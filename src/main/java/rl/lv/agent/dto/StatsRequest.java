package rl.lv.agent.dto;

import java.io.Serializable;

public class StatsRequest implements Serializable {
	
	private static final long serialVersionUID = -220634744043486180L;
	private String log;
	private LogStructure logStructure;
	
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public LogStructure getLogStructure() {
		return logStructure;
	}
	public void setLogStructure(LogStructure logStructure) {
		this.logStructure = logStructure;
	}

}
