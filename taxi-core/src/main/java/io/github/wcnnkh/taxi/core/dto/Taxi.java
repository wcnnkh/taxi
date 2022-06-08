package io.github.wcnnkh.taxi.core.dto;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Taxi extends Trace {
	private static final long serialVersionUID = 1L;
	@Schema(description = "车辆状态")
	private TaxiStatus taxiStatus;
}
