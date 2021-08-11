package io.github.wcnnkh.taxi.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class NearbyTaxiQuery implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "位置", required = true)
	@NotNull
	private Location location;
	@Schema(description = "车辆状态")
	private TaxiStatus taxiStatus;
	@Schema(description = "查询的范围，单位：米", required = true, defaultValue = "5000")
	@NotNull
	private Long distance = 5000L;
	@Schema(description = "返回的最大数量", required = true, defaultValue = "10")
	@Min(10)
	@NotNull
	private int count = 10;

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

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
