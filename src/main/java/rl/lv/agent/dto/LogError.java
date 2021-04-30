package rl.lv.agent.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LogError implements Serializable {

	private static final long serialVersionUID = 268865123500879220L;
	private String date;
	private String level;
	private String message;
	private String reqid;
	private String user;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReqid() {
		return reqid;
	}

	public void setReqid(String reqid) {
		this.reqid = reqid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
