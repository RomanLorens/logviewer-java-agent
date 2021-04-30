package rl.lv.agent.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LogDetails implements Serializable {

	private static final long serialVersionUID = 5905672322087806310L;
	private String host;
	private String name;
	private long modtime;
	private long size;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getModtime() {
		return modtime;
	}

	public void setModtime(long modtime) {
		this.modtime = modtime;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
