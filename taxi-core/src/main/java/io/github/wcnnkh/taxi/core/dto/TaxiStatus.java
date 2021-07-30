package io.github.wcnnkh.taxi.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TaxiStatus extends Member {
	private static final long serialVersionUID = 1L;
	@Schema(description = "是否上班中")
	private boolean working;
	@Schema(description = "是否营运中")
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
