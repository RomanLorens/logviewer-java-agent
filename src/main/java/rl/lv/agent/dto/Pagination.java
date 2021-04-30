package rl.lv.agent.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Pagination implements Serializable {

	private static final long serialVersionUID = 989120485629529002L;
	private int from;
	private int size;
	private long total;

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

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
