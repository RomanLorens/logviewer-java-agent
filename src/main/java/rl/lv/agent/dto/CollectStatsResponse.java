package rl.lv.agent.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CollectStatsResponse implements Serializable {

	private static final long serialVersionUID = -2220221653020884138L;
	private Map<String, Map<String, Long>> users = new HashMap<>();
	private long totalRequests;

	public Map<String, Map<String, Long>> getUsers() {
		return users;
	}

	public void setUsers(Map<String, Map<String, Long>> users) {
		this.users = users;
	}

	public long getTotalRequests() {
		return totalRequests;
	}

	public void setTotalRequests(long totalRequests) {
		this.totalRequests = totalRequests;
	}

	public void incrementRequests() {
		this.totalRequests++;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
