package io.github.wcnnkh.taxi.core.enums;

public enum OrderStatus {
	RECORD("000", "录单"), PRE_CONFIRM("100", "预确认接口"), CONFIRM_TIMEOUT("110", "确认超时"), CONFIRM("200", "确认接单"),
	RECEIVE_PASSENGER("210", "接到乘客"), ARRIVE("220", "到达目的地"), NO_SUPPLY("400", "无供"), PASSENGER_CANCEL("510", "乘客取消"),
	TAXI_CANCEL("520", "司机取消"),;

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

	public static boolean canGrabOrder(String status) {
		return RECORD.getCode().equals(status);
	}
}
