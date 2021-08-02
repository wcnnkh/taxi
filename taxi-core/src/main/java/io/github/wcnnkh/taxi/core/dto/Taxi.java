package io.github.wcnnkh.taxi.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class Taxi extends Trace {
	private static final long serialVersionUID = 1L;
	@Schema(description = "车辆状态")
	private TaxiStatus taxiStatus;

	public TaxiStatus getTaxiStatus() {
		return taxiStatus;
	}

	public void setTaxiStatus(TaxiStatus taxiStatus) {
		this.taxiStatus = taxiStatus;
	}
}
