package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import scw.orm.annotation.PrimaryKey;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "下单请求")
public class PostOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "订单号", required = true)
	@NotNull
	@PrimaryKey
	private String orderId;
	@Schema(description = "下单的乘客id")
	private String passengerId;
	@Schema(description = "上车地点")
	private Location startLocation;
	@Schema(description = "下车地点")
	private Location endLocation;
	@Schema(description = "调度超时时间")
	private Long dispatchTimeout;

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

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

	public Long getDispatchTimeout() {
		return dispatchTimeout;
	}

	public void setDispatchTimeout(Long dispatchTimeout) {
		this.dispatchTimeout = dispatchTimeout;
	}
}
