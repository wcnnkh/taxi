package io.github.wcnnkh.taxi.core.dto;

import javax.validation.constraints.NotNull;

import io.basc.framework.orm.annotation.Entity;
import io.basc.framework.orm.annotation.PrimaryKey;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Order extends PostOrderRequest {
	private static final long serialVersionUID = 1L;
	@Schema(description = "订单号", required = true)
	@NotNull
	@PrimaryKey
	private String id;
	@Schema(description = "该订单对应的车辆(如果为空说明还未分配到司机)")
	private String taxiId;
	@Schema(description = "订单状态", enumAsRef = true, oneOf = OrderStatus.class)
	private String status;
	@Schema(description = "订单创建时间")
	private Long createTime;
}
