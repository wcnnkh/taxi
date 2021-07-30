package io.github.wcnnkh.taxi.core.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

public class Passenger extends Member{
	private static final long serialVersionUID = 1L;
	@Schema(description = "扩展信息")
	private Map<String, String> extendMap;
	
	public Map<String, String> getExtendMap() {
		return extendMap;
	}
	public void setExtendMap(Map<String, String> extendMap) {
		this.extendMap = extendMap;
	}
}
