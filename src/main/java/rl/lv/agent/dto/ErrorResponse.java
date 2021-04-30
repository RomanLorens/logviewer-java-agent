package rl.lv.agent.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = -700946917544781284L;
	private List<LogError> errors = new ArrayList<>();
	private Pagination pagination;

	public List<LogError> getErrors() {
		return errors;
	}

	public void setErrors(List<LogError> errors) {
		this.errors = errors;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
