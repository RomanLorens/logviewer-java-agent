package rl.lv.agent.support.dto;

import java.io.Serializable;
import java.util.List;

public class VersionInfo implements Serializable {

	private static final long serialVersionUID = 8995912583094729429L;
	private String host;
	private long pid;
	private List<String> jvmArgs;
	private String uptime;

	public String getHost() {
		return host;
	}

	public void setHost(String ip) {
		this.host = ip;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long p) {
		this.pid = p;
	}

	public List<String> getJVMArgs() {
		return jvmArgs;
	}

	public void setJVMArgs(List<String> args) {
		this.jvmArgs = args;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

}
