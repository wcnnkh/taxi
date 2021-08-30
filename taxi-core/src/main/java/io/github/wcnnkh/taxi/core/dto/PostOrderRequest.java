package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.basc.framework.mapper.MapperUtils;
import io.basc.framework.orm.annotation.Entity;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "下单请求")
public class PostOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "下单的乘客id")
	private String passengerId;
	@Schema(description = "上车地点")
	@NotNull
	private Location startLocation;
	@Schema(description = "下车地点")
	private Location endLocation;
	@Schema(description = "调度超时时间, 单位(毫秒)")
	private Long dispatchTimeout;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

	public Long getDispatchTimeout() {
		return dispatchTimeout;
	}

	public void setDispatchTimeout(Long dispatchTimeout) {
		this.dispatchTimeout = dispatchTimeout;
	}
	
	@Override
	public String toString() {
		return MapperUtils.toString(this);
	}
}
