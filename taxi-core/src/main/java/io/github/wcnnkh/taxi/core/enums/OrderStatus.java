package io.github.wcnnkh.taxi.core.enums;

public enum OrderStatus {
	RECORD("000", "录单"), 
	CONFIRM_DRIVER("100", "司机接单"),
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
