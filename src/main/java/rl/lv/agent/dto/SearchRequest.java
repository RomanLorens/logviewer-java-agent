package rl.lv.agent.dto;

public class SearchRequest extends ListLogsRequest {

	private static final long serialVersionUID = 1598241296598602751L;

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
