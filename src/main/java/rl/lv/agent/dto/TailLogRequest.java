package rl.lv.agent.dto;

import java.io.Serializable;

public class TailLogRequest implements Serializable {

	private static final long serialVersionUID = -5918531570111502949L;
	private String log;

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
}
