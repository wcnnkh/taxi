package io.github.wcnnkh.taxi.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PostOrder extends PostOrderRequest {
	private static final long serialVersionUID = 1L;
	@Schema(description = "该订单对应的车辆(如果为空说明还未分配到司机)")
	private String taxiId;

	public String getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(String taxiId) {
		this.taxiId = taxiId;
	}
}
