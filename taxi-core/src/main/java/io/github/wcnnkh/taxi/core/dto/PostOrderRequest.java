package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "下单请求")
public class PostOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "下单的乘客id")
	private String passengerId;
	@Schema(description = "订单号")
	private String orderId;
	@Schema(description = "上车地点")
	private Location startLocation;
	@Schema(description = "下车地点")
	private Location endLocation;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
