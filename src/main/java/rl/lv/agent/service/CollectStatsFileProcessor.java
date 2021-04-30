package rl.lv.agent.service;

import java.util.HashMap;
import java.util.Map;

import rl.lv.agent.dto.CollectStatsResponse;


class CollectStatsFileProcessor implements FileProcessor {

	private CollectStatsResponse res = new CollectStatsResponse();
	private String date;

	public CollectStatsFileProcessor(String date) {
		this.date = date;
	}

	@Override
	public void process(TokenValues t) {
		if (!t.date.contains(date)) {
			return;
		}
		res.getUsers().computeIfAbsent(t.user, k -> new HashMap<String, Long>());
		Map<String, Long> userLevels = res.getUsers().get(t.user);
		userLevels.compute(t.level, (k, v) -> v == null ? 1 : v + 1);
		res.incrementRequests();
	}

	public CollectStatsResponse getResult() {
		return res;
	}

}
