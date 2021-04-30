package rl.lv.agent.support.dto;

import java.io.Serializable;
import java.util.List;

public class MemoryStats implements Serializable {

	private static final long serialVersionUID = -5457108465172204207L;
	private String totalMemory;
	private String freeMemory;
	private String maxMemory;
	private int cpus;
	private double cpuLoadPercentage;
	private int threadsCount;
	private List<ThreadDetails> threads;

	public String getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(String totalMemory) {
		this.totalMemory = totalMemory;
	}

	public String getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}

	public int getCpus() {
		return cpus;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	public int getThreadsCount() {
		return threadsCount;
	}

	public void setThreadsCount(int threadsCount) {
		this.threadsCount = threadsCount;
	}

	public List<ThreadDetails> getThreads() {
		return threads;
	}

	public void setThreads(List<ThreadDetails> threads) {
		this.threads = threads;
	}

	public String getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(String maxMemory) {
		this.maxMemory = maxMemory;
	}

	public double getCpuLoadPercentage() {
		return cpuLoadPercentage;
	}

	public void setCpuLoadPercentage(double cpuLoadPercentage) {
		this.cpuLoadPercentage = cpuLoadPercentage;
	}

}
