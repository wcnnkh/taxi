package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.basc.framework.mapper.MapperUtils;
import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 位置
 * @author shuchaowen
 *
 */
@Entity
@Schema(description = "位置")
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "经度", required = true)
	@NotNull
	private Double longitude;
	@Schema(description = "纬度", required = true)
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
	
	@Override
	public String toString() {
		return MapperUtils.toString(this);
	}
}
