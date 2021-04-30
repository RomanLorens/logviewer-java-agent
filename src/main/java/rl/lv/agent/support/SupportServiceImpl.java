package rl.lv.agent.support;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rl.lv.agent.support.dto.MemoryStats;
import rl.lv.agent.support.dto.ThreadDetails;
import rl.lv.agent.support.dto.VersionInfo;


public class SupportServiceImpl implements SupportService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static long kilo = 1024;
	private static long mega = kilo * kilo;
	private static long giga = mega * kilo;
	private static long tera = giga * kilo;

	@Override
	public MemoryStats memoryDiagnostics() {
		MemoryStats ms = new MemoryStats();
		ms.setTotalMemory(convertBytes(Runtime.getRuntime().totalMemory()));
		ms.setFreeMemory(convertBytes(Runtime.getRuntime().freeMemory()));
		ms.setMaxMemory(convertBytes(Runtime.getRuntime().maxMemory()));
		ms.setCpus(Runtime.getRuntime().availableProcessors());
		List<ThreadDetails> threads = Thread.getAllStackTraces().keySet().stream().map(t -> {
			ThreadDetails td = new ThreadDetails();
			td.setActive(t.isAlive());
			td.setDeamon(t.isDaemon());
			td.setName(t.getName());
			td.setPriortity(t.getPriority());
			td.setState(t.getState());
			return td;
		}).collect(Collectors.toList());
		ms.setThreads(threads);
		ms.setThreadsCount(threads.size());
		ms.setCpuLoadPercentage(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
		return ms;
	}

	@Override
	public VersionInfo version() {
		VersionInfo version = new VersionInfo();
		try {
			version.setHost(InetAddress.getLocalHost().toString());
		} catch (UnknownHostException e) {
			logger.error("Could not get innet address {}".concat(e.getMessage()));
		}
		version.setPid(getAppPort());
		version.setJVMArgs(ManagementFactory.getRuntimeMXBean().getInputArguments());
		version.setUptime(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()).toString());
		return version;
	}

	private long getAppPort() {
		String process = ManagementFactory.getRuntimeMXBean().getName();
		int pos = process.indexOf("@");
		if (pos != -1) {
			try {
				return Long.parseLong(process.substring(0, pos));
			} catch (Exception e) {
				logger.error("Could not get app process '{}' port, {}", process, e.getMessage());
			}
		}
		return 0;
	}

	private String convertBytes(long size) {
		String s = "";
		double kb = (double) size / kilo;
		double mb = kb / kilo;
		double gb = mb / kilo;
		double tb = gb / kilo;
		if (size < kilo) {
			s = size + " Bytes";
		} else if (size >= kilo && size < mega) {
			s = String.format("%.2f", kb) + " KB";
		} else if (size >= mega && size < giga) {
			s = String.format("%.2f", mb) + " MB";
		} else if (size >= giga && size < tera) {
			s = String.format("%.2f", gb) + " GB";
		} else if (size >= tera) {
			s = String.format("%.2f", tb) + " TB";
		}
		return s;
	}

	@Override
	public void stopServer() {
		logger.info("Stopping server...");
		long port = getAppPort();
		if (port > 0) {
			String os = System.getProperty("os.name");
			try {
				if (os != null && os.toLowerCase().contains("windows")) {
					Runtime.getRuntime().exec("taskkill /F /PID " + port);
				} else if (os != null) {
					Runtime.getRuntime().exec("kill -9 " + port);
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

}
