package rl.lv.agent.dto;

import java.io.Serializable;
import java.util.List;

public class ListLogsRequest implements Serializable {

	private static final long serialVersionUID = 3163622013070226568L;
	private List<String> logs;

	public List<String> getLogs() {
		return logs;
	}

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}

}
