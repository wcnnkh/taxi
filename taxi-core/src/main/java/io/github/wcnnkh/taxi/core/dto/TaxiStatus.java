package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Data
public class TaxiStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "是否上班中")
	private boolean working;
	@Schema(description = "是否营运中(是否有乘客)")
	private boolean operation;
}
