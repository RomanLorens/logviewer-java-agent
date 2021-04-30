package rl.lv.agent.dto;

public class StatsRequestPagination extends StatsRequest {

	private static final long serialVersionUID = -3040092501314970393L;
	private int from;
	private int size;

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
