package io.github.wcnnkh.taxi.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class Member extends Location{
	private static final long serialVersionUID = 1L;
	@Schema(description = "成员id，如果是乘客就是乘客id，如果是车就是车的id")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
