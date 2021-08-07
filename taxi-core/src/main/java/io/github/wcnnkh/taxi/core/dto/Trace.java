package io.github.wcnnkh.taxi.core.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class Trace implements Serializable {
	private static final long serialVersionUID = 1L;
	@Schema(description = "成员id，如果是乘客就是乘客id，如果是车就是车的id", required = true)
	@NotNull
	private String id;
	@Schema(description = "位置")
	private TraceLocation location;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TraceLocation getLocation() {
		return location;
	}

	public void setLocation(TraceLocation location) {
		this.location = location;
	}
}
