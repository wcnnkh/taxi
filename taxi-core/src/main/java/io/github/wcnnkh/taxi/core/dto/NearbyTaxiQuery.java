package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Entity
public class NearbyTaxiQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "位置", required = true)
	@NotNull
	private Location location;
	@Schema(description = "车辆状态")
	private TaxiStatus taxiStatus;
	@Schema(description = "查询的范围，单位：千米", required = true, defaultValue = "5")
	@NotNull
	private Double distance = 5D;
	@Schema(description = "返回的最大数量", required = true, defaultValue = "50")
	@Min(1)
	@NotNull
	private Integer count = 50;
}
