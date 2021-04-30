package rl.lv.agent.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Stats implements Serializable {

	private static final long serialVersionUID = 7501238475002422198L;
	private long counter;
	private String lastTime;
	private Map<String, Long> leveles = new HashMap<>();
	private List<ErrorDetails> errors;
	private List<ErrorDetails> warnings;

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public Map<String, Long> getLeveles() {
		return leveles;
	}

	public void setLeveles(Map<String, Long> leveles) {
		this.leveles = leveles;
	}

	public List<ErrorDetails> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorDetails> errors) {
		this.errors = errors;
	}

	public List<ErrorDetails> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<ErrorDetails> warnings) {
		this.warnings = warnings;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public void incrementCounter() {
		this.counter++;
	}
}
