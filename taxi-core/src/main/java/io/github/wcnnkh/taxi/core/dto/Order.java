package io.github.wcnnkh.taxi.core.dto;

import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class Order extends PostOrderRequest {
	private static final long serialVersionUID = 1L;
	@Schema(description = "该订单对应的车辆(如果为空说明还未分配到司机)")
	private String taxiId;
	@Schema(description = "订单状态", enumAsRef = true, oneOf = OrderStatus.class)
	private String status;
	@Schema(description = "订单创建时间")
	private Long createTime;

	public String getTaxiId() {
		return taxiId;
	}

	public void setTaxiId(String taxiId) {
		this.taxiId = taxiId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
}
