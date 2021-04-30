package rl.lv.agent.dto;

import java.io.Serializable;

public class CollectStatsRequest implements Serializable {

	private static final long serialVersionUID = -3380901536381478872L;
	private LogStructure logStructure;
	private String date;
	private String logPath;

	public LogStructure getLogStructure() {
		return logStructure;
	}

	public void setLogStructure(LogStructure logStructure) {
		this.logStructure = logStructure;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

}
