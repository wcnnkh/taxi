package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

public class NearbyTaxiQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	private Location location;
	private TaxiStatus taxiStatus;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public TaxiStatus getTaxiStatus() {
		return taxiStatus;
	}

	public void setTaxiStatus(TaxiStatus taxiStatus) {
		this.taxiStatus = taxiStatus;
	}
}
