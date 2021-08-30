package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateOrderStatusRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "订单号", required = true)
	@NotNull
	private String orderId;
	@Schema(description = "订单状态", required = true)
	@NotNull
	private OrderStatus status;
	
	@Schema(description = "车辆id")
	private String taxiId;
	@Schema(description = "乘客id")
	private String passengerId;
	@Schema(description = "描述")
	private String desc;
	
	public UpdateOrderStatusRequest(){
	}
	
	public UpdateOrderStatusRequest(String orderId, OrderStatus status){
		this.orderId = orderId;
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(String taxiId) {
		this.taxiId = taxiId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}
