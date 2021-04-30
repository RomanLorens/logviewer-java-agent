package rl.lv.agent.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SearchResponse implements Serializable {

	private static final long serialVersionUID = 9204244033525581395L;
	private String host;
	private List<String> lines;
	private String logfile;
	private long time;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public String gelogfile() {
		return logfile;
	}

	public void setlogfile(String logfile) {
		this.logfile = logfile;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
