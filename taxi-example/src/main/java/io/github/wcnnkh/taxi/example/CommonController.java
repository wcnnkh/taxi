package io.github.wcnnkh.taxi.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import scw.util.XUtils;

@Path("/common")
public class CommonController {
	@Path("/uuid")
	@GET
	public String uuid() {
		return XUtils.getUUID();
	}
}
