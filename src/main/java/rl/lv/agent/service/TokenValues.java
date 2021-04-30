package rl.lv.agent.service;

class TokenValues {

	final String date;
	final String level;
	final String message;
	final String reqid;
	final String user;

	TokenValues(String date, String level, String message, String reqid, String user) {
		this.date = date != null ? date : "";
		this.level = level != null ? level : "";
		this.message = message != null ? message : "";
		this.reqid = reqid != null ? reqid : "";
		this.user = user != null ? user : "";
	}

}
