package rl.lv.agent.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ErrorDetails implements Serializable {

	private static final long serialVersionUID = -859231535345931208L;
	private String date;
	private String reqid;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReqid() {
		return reqid;
	}

	public void setReqid(String reqid) {
		this.reqid = reqid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
