package io.github.wcnnkh.taxi.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 轨迹位置
 * 
 * @author shuchaowen
 *
 */
@Schema(description = "轨迹位置")
public class TraceLocation extends Location {
	private static final long serialVersionUID = 1L;
	@Schema(description = "速度")
	private Float speed;
	@Schema(description = "方向角")
	private Float bearing;
	@Schema(description = "定位时间")
	private Long time;

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Float getBearing() {
		return bearing;
	}

	public void setBearing(Float bearing) {
		this.bearing = bearing;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}
