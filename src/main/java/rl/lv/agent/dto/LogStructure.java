package rl.lv.agent.dto;

import java.io.Serializable;

public class LogStructure implements Serializable {

	private static final long serialVersionUID = -9141206309295849594L;
	private int date;
	private String dateFormat;
	private int level;
	private int message;
	private int reqid;
	private int user;
	private String javaDateFormat;

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public int getReqid() {
		return reqid;
	}

	public void setReqid(int reqid) {
		this.reqid = reqid;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public String getJavaDateFormat() {
		return javaDateFormat;
	}

	public void setJavaDateFormat(String javaDateFormat) {
		this.javaDateFormat = javaDateFormat;
	}

}
