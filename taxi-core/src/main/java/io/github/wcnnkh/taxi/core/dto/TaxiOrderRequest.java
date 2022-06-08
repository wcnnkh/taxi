package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Data
public class TaxiOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "订单号")
	@NotNull(message = "订单号不能为空")
	private String orderId;
	@Schema(description = "车辆id")
	@NotNull(message = "车辆id不能为空")
	private String taxiId;
}
