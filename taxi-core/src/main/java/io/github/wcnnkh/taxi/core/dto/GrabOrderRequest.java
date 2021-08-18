package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "抢单请求")
public class GrabOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "订单号")
	private String orderId;
	@Schema(description = "抢单车辆")
	private String taxiId;

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
	
	@Override
	public String toString() {
		return MapperUtils.toString(this);
	}
}
