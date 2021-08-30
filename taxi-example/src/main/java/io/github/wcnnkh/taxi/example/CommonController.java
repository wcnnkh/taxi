package io.github.wcnnkh.taxi.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import io.basc.framework.util.XUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "公共接口")
@Path("/common")
public class CommonController {
	@Operation(description = "生成一个随机id")
	@Path("/uuid")
	@GET
	public String uuid() {
		return XUtils.getUUID();
	}
}
