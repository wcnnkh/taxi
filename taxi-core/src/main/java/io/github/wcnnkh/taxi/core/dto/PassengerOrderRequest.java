package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import scw.orm.annotation.Entity;

@Entity
public class PassengerOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "订单号", required = true)
	private String orderId;
	@Schema(description = "乘客id", required = true)
	private String passengerId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}
}
