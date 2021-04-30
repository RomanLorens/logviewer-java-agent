package rl.lv.agent.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import rl.lv.agent.dto.CollectStatsRequest;
import rl.lv.agent.dto.CollectStatsResponse;
import rl.lv.agent.dto.ErrorResponse;
import rl.lv.agent.dto.ListLogsRequest;
import rl.lv.agent.dto.LogDetails;
import rl.lv.agent.dto.LogStructure;
import rl.lv.agent.dto.SearchRequest;
import rl.lv.agent.dto.SearchResponse;
import rl.lv.agent.dto.Stats;
import rl.lv.agent.dto.StatsRequest;
import rl.lv.agent.dto.StatsRequestPagination;

public class LogViewerServiceTest {

	private static CompletionExecutor executor = new CompletionExecutor();
	private LogViewerServiceImpl service = new LogViewerServiceImpl(executor);

	@Test
	public void listLogsTest() {
		ListLogsRequest req = new ListLogsRequest();
		req.setLogs(Arrays.asList("src/test/resources/logs/bcsviewer.log"));

		List<LogDetails> res = service.listLogs(req);

		assertTrue(!res.isEmpty());
	}

	@Test
	public void grepLogsTest() {
		SearchRequest req = new SearchRequest();
		req.setLogs(Arrays.asList("src/test/resources/logs/bcsviewer.log"));
		req.setValue("rl78794");

		List<SearchResponse> res = service.search(req);

		assertEquals(1, res.size());
	}

	@Test
	public void tailLogTest() {
		SearchResponse res = service.tail("src/test/resources/logs/bcsviewer.log");

		assertTrue(!res.getLines().isEmpty());
	}

	@Test
	public void statsTest() {
		StatsRequest req = new StatsRequest();
		req.setLog("src/test/resources/logs/bcsviewer.log");
		LogStructure ls = new LogStructure();
		ls.setDate(0);
		ls.setDateFormat("2006-01-02");
		ls.setLevel(2);
		ls.setMessage(6);
		ls.setReqid(5);
		ls.setUser(4);
		req.setLogStructure(ls);

		Map<String, Stats> stats = service.stats(req);

		assertTrue(stats.size() > 0);
		;
	}

	@Test
	public void errosTest() {
		StatsRequestPagination req = new StatsRequestPagination();
		req.setLog("src/test/resources/logs/bcsviewer.log");
		LogStructure ls = new LogStructure();
		ls.setDate(0);
		ls.setDateFormat("2006-01-02");
		ls.setLevel(2);
		ls.setMessage(6);
		ls.setReqid(5);
		ls.setUser(4);
		req.setLogStructure(ls);
		req.setFrom(0);
		req.setSize(5);

		ErrorResponse errors = service.errors(req);

		assertTrue(errors.getErrors().size() > 0);
	}

	@Test
	public void collectStatsTest() {
		CollectStatsRequest req = new CollectStatsRequest();
		req.setLogPath("src/test/resources/logs/bcsviewer.log");
		LogStructure ls = new LogStructure();
		ls.setJavaDateFormat("yyyy-MM-dd");
		ls.setDate(0);
		ls.setLevel(2);
		ls.setMessage(6);
		ls.setReqid(5);
		ls.setUser(4);
		req.setLogStructure(ls);
		req.setDate("2021-04-20");

		CollectStatsResponse res = service.collectStats(req);

		assertTrue(res.getUsers().size() > 0);
	}

	@AfterAll
	public static void cleanUp() {
		executor.shutdown();
	}

}
