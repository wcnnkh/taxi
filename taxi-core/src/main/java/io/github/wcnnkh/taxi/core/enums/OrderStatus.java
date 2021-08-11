package io.github.wcnnkh.taxi.core.enums;

public enum OrderStatus {
	RECORD("000", "录单"), 
	PRE_CONFIRM("100", "预确认接口"),
	CONFIRM_TIMEOUT("110", "确认超时"),
	CONFIRM("200", "确认接单"),
	NO_SUPPLY("300", "无供");

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
	
	/**
	 * 此状态是否可以进行抢单调度
	 * @param status
	 * @return
	 */
	public static boolean canGrabOrder(String status) {
		return RECORD.getCode().equals(status);
	}
}
