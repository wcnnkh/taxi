package io.github.wcnnkh.taxi.core.enums;

public enum OrderStatus {
	RECORD("000", "录单"), 
	PRE_CONFIRM("100", "预确认接口"),
	CONFIRM("110", "确认接单"),
	
	CANCEL("500", "取消"),
	NO_SUPPLY("510", "无供");

	private final String code;
	private final String description;

	private OrderStatus(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
