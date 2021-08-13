package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import scw.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class TaxiStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "是否上班中")
	private boolean working;
	@Schema(description = "是否营运中(是否有乘客)")
	private boolean operation;

	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}

	public boolean isOperation() {
		return operation;
	}

	public void setOperation(boolean operation) {
		this.operation = operation;
	}
}
