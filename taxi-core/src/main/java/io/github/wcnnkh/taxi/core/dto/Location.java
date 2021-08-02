package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 位置
 * @author shuchaowen
 *
 */
@Schema(description = "位置")
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "经度")
	@NotNull
	private Double longitude;
	@Schema(description = "纬度")
	@NotNull
	private Double latitude;

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
}
