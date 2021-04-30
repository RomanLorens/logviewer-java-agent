package rl.lv.agent.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rl.lv.agent.dto.CollectStatsRequest;
import rl.lv.agent.dto.CollectStatsResponse;
import rl.lv.agent.dto.ErrorDetails;
import rl.lv.agent.dto.ErrorResponse;
import rl.lv.agent.dto.ListLogsRequest;
import rl.lv.agent.dto.LogDetails;
import rl.lv.agent.dto.LogError;
import rl.lv.agent.dto.LogStructure;
import rl.lv.agent.dto.Pagination;
import rl.lv.agent.dto.SearchRequest;
import rl.lv.agent.dto.SearchResponse;
import rl.lv.agent.dto.Stats;
import rl.lv.agent.dto.StatsRequest;
import rl.lv.agent.dto.StatsRequestPagination;

public class LogViewerServiceImpl implements LogViewerService {

	private static final String LOGVIEWER_HOST_ENV = "LOGVIEWER_HOST";
	private static final int MAX_LILNES = 100;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final CompletionExecutor executor;

	public LogViewerServiceImpl(CompletionExecutor executor) {
		this.executor = executor;
	}

	@Override
	public List<LogDetails> listLogs(ListLogsRequest req) {
		List<Path> dirs = req.getLogs().stream().map(l -> {
			Path path = Paths.get(l);
			Path parent = path.getParent();
			if (parent == null) {
				logger.error("Invalid logs path {}", l);
				return null;
			}
			return parent;
		}).filter(d -> d != null && d.toFile().isDirectory()).distinct().collect(Collectors.toList());

		if (dirs.isEmpty()) {
			return Collections.emptyList();
		}

		String host = getHost();
		dirs.forEach(dir -> {
			executor.submit(() -> {
				return listLogs(dir, host);
			});
		});

		return executor.waitForTasks(dirs.size(), LogDetails.class);
	}

	private String getHost() {
		String host;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.error("Could not get host name {}", e.getMessage());
			host = System.getenv(LOGVIEWER_HOST_ENV);
		}
		return host;
	}

	private List<LogDetails> listLogs(Path dir, String host) {
		try {
			return Files.list(dir).map(f -> {
				File file = f.toFile();
				if (!file.isFile()) {
					return null;
				}
				LogDetails ld = new LogDetails();
				ld.setName(f.toAbsolutePath().toString());
				ld.setModtime(file.lastModified());
				ld.setSize(file.length());
				ld.setHost(host);
				return ld;
			}).filter(f -> f != null).collect(Collectors.toList());
		} catch (IOException e) {
			logger.error("could not list dir '{}', {}", dir.toString(), e.getMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public List<SearchResponse> search(SearchRequest req) {
		AtomicInteger threads = new AtomicInteger();
		String host = getHost();
		req.getLogs().forEach(log -> {
			threads.incrementAndGet();
			executor.submit(() -> {
				List<SearchResponse> res = new ArrayList<>();
				res.add(grep(log, req.getValue()));
				res.forEach(rs -> rs.setHost(host));
				return res;
			});
		});

		return executor.waitForTasks(threads.get(), SearchResponse.class);
	}

	@Override
	public SearchResponse tail(String log) {
		long start = System.currentTimeMillis();
		SearchResponse res = new SearchResponse();
		res.setHost(getHost());
		res.setlogfile(log);
		res.setLines(new ArrayList<String>());
		try (RandomAccessFile raf = new RandomAccessFile(new File(log), "r")) {
			long length = raf.length() - 1;
			raf.seek(length);
			int lines = 0;
			StringBuilder sb = new StringBuilder();
			for (long pointer = length; pointer >= 0 && lines < MAX_LILNES; pointer--) {
				raf.seek(pointer);
				char c = (char) raf.read();
				sb.append(c);
				if (c == '\n') {
					String l = sb.reverse().toString().trim();
					if (!l.isEmpty()) {
						res.getLines().add(l);
						lines++;
					}
					sb.setLength(0);
				}
			}
			res.setTime(System.currentTimeMillis() - start);
		} catch (Exception e) {
			throw new IllegalArgumentException(log + " could not be read", e);
		}
		return res;
	}
	
	private SearchResponse grep(String log, String value) {
		logger.info("Grep {} - {}", log, value);
		long start = System.currentTimeMillis();
		SearchResponse res = new SearchResponse();
		res.setlogfile(log);
		res.setLines(new ArrayList<String>());
		value = value.toLowerCase();
		try (Scanner scanner = new Scanner(new File(log))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.toLowerCase().contains(value)) {
					res.getLines().add(line);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("Grep failed - wrong path {}", e.getMessage());
		}
		res.setTime(System.currentTimeMillis() - start);
		return res;
	}

	@Override
	public Map<String, Stats> stats(StatsRequest req) {
		Map<String, Stats> res = new HashMap<>();
		FileProcessor fp = (t) -> {
			res.computeIfAbsent(t.user, (k) -> new Stats());
			Stats stats = res.get(t.user);
			IsError isError = isError(t.level);
			if (isError.error || isError.warn) {
				ErrorDetails err = new ErrorDetails();
				err.setDate(t.date);
				err.setReqid(t.reqid);
				List<ErrorDetails> details = errorDetails(stats, isError);
				details.add(err);
			}
			stats.getLeveles().compute(t.level, (k, v) -> v == null ? 1 : v + 1);
			stats.incrementCounter();
			stats.setLastTime(t.date);
		};
		processFile(req.getLog(), req.getLogStructure(), fp);
		return res;
	}

	@Override
	public CollectStatsResponse collectStats(CollectStatsRequest req) {
		SimpleDateFormat df = new SimpleDateFormat(req.getLogStructure().getJavaDateFormat());
		String date;
		Date _date;
		try {
			_date = df.parse(req.getDate());
			date = df.format(_date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format " + req.getDate(), e);
		}

		long dateMilis = _date.getTime();
		String fileName = Paths.get(req.getLogPath()).getFileName().toString();
		String filePattern = fileName.substring(0, fileName.indexOf("."));
		Path dir = Paths.get(req.getLogPath()).getParent();
		List<Path> logs;
		try {
			logs = Files.list(dir).filter(p -> {
				return p.getFileName().toString().contains(filePattern) && p.toFile().lastModified() >= dateMilis;
			}).collect(Collectors.toList());
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not list folder " + dir.toString(), e);
		}

		logs.forEach(log -> {
			executor.submit(() -> {
				logger.info("Collect stats for {}, {}", log.toString(), date);
				CollectStatsFileProcessor fp = new CollectStatsFileProcessor(date);
				processFile(log.toString(), req.getLogStructure(), fp);
				return fp.getResult();
			});
		});

		List<CollectStatsResponse> results = executor.waitForTasks(logs.size(), CollectStatsResponse.class);
		CollectStatsResponse res = new CollectStatsResponse();
		results.forEach(r -> {
			res.setTotalRequests(res.getTotalRequests() + r.getTotalRequests());
			r.getUsers().forEach((k, v) -> {
				res.getUsers().computeIfAbsent(k, (u) -> new HashMap<String, Long>());
				Map<String, Long> userLevels = res.getUsers().get(k);
				v.forEach((level, count) -> {
					userLevels.compute(level, (_level, _counter) -> _counter == null ? count : count + _counter);
				});
			});
		});
		return res;
	}

	private int maxLevel(LogStructure ls) {
		return Collections
				.max(Arrays.asList(ls.getDate(), ls.getLevel(), ls.getMessage(), ls.getReqid(), ls.getUser()));
	}

	private List<ErrorDetails> errorDetails(Stats stats, IsError isError) {
		List<ErrorDetails> details;
		if (isError.error) {
			details = stats.getErrors();
			if (details == null) {
				details = new ArrayList<>();
				stats.setErrors(details);
			}
		} else {
			details = stats.getWarnings();
			if (details == null) {
				details = new ArrayList<>();
				stats.setWarnings(details);
			}
		}
		return details;
	}

	private class IsError {
		private boolean error;
		private boolean warn;
	}

	private IsError isError(String level) {
		IsError e = new IsError();
		e.error = level.equals("ERROR");
		e.warn = level.contains("WARN");
		return e;
	}

	@Override
	public ErrorResponse errors(StatsRequestPagination req) {
		ErrorResponse res = new ErrorResponse();
		FileProcessor fp = (t) -> {
			IsError isError = isError(t.level);
			if (!(isError.error || isError.warn)) {
				return;
			}
			LogError err = new LogError();
			err.setDate(t.date);
			err.setLevel(t.level);
			err.setMessage(t.message);
			err.setReqid(t.reqid);
			err.setUser(t.user);
			res.getErrors().add(err);
		};
		processFile(req.getLog(), req.getLogStructure(), fp);

		Pagination pagination = new Pagination();
		pagination.setFrom(req.getFrom());
		pagination.setSize(req.getSize());
		pagination.setTotal(res.getErrors().size());
		res.setPagination(pagination);

		Collections.reverse(res.getErrors());

		if (!res.getErrors().isEmpty()) {
			int from = req.getFrom() * req.getSize();
			int to = from + req.getSize();
			if (to >= res.getErrors().size()) {
				to = res.getErrors().size();
			}
			if (from > res.getErrors().size()) {
				from = res.getErrors().size();
			}
			res.setErrors(res.getErrors().subList(from, to));
		}
		return res;
	}

	private void processFile(String log, LogStructure ls, FileProcessor fp) {
		int max = maxLevel(ls);
		AtomicLong systemCounter = new AtomicLong();
		Set<String> requests = new HashSet<>();
		try (Stream<String> stream = Files.lines(Paths.get(log))) {
			stream.forEach(l -> {
				String[] split = l.split("\\|");
				if (split.length <= max) {
					return;
				}
				String user = split[ls.getUser()].toLowerCase().trim();
				if (user.isEmpty()) {
					user = "system";
				}
				String reqid = split[ls.getReqid()];
				if (reqid.isEmpty()) {
					reqid = user + systemCounter.getAndIncrement();
				}
				String level = split[ls.getLevel()].toUpperCase();
				String key = reqid + level + user;
				if (!requests.add(key)) {
					return;
				}
				fp.process(new TokenValues(split[ls.getDate()], level, split[ls.getMessage()], reqid, user));
			});
		} catch (Exception e) {
			throw new IllegalArgumentException("errors failed", e);
		}
	}

}
