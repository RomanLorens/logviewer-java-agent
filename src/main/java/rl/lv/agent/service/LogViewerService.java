package rl.lv.agent.service;

import java.util.List;
import java.util.Map;

import rl.lv.agent.dto.CollectStatsRequest;
import rl.lv.agent.dto.CollectStatsResponse;
import rl.lv.agent.dto.ErrorResponse;
import rl.lv.agent.dto.ListLogsRequest;
import rl.lv.agent.dto.LogDetails;
import rl.lv.agent.dto.SearchRequest;
import rl.lv.agent.dto.SearchResponse;
import rl.lv.agent.dto.Stats;
import rl.lv.agent.dto.StatsRequest;
import rl.lv.agent.dto.StatsRequestPagination;

public interface LogViewerService {

	List<LogDetails> listLogs(ListLogsRequest req);

	List<SearchResponse> search(SearchRequest req);

	SearchResponse tail(String log);

	Map<String, Stats> stats(StatsRequest req);

	ErrorResponse errors(StatsRequestPagination req);

	CollectStatsResponse collectStats(CollectStatsRequest req);

}
