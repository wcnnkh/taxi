package io.github.wcnnkh.taxi.core.dto;

import scw.mapper.MapperUtils;
import scw.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
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
	
	@Override
	public String toString() {
		return MapperUtils.toString(this);
	}
}
