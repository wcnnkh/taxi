package io.github.wcnnkh.taxi.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import scw.mapper.MapperUtils;
import scw.orm.annotation.Entity;

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

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return MapperUtils.toString(this);
	}
}
