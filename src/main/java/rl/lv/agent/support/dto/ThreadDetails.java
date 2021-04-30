package rl.lv.agent.support.dto;

import java.io.Serializable;
import java.lang.Thread.State;

public class ThreadDetails implements Serializable {

	private static final long serialVersionUID = -338712865371655934L;
	private String name;
	private boolean active;
	private int priortity;
	private boolean deamon;
	private State state;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getPriortity() {
		return priortity;
	}

	public void setPriortity(int priortity) {
		this.priortity = priortity;
	}

	public boolean isDeamon() {
		return deamon;
	}

	public void setDeamon(boolean deamon) {
		this.deamon = deamon;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}
}
