package io.github.wcnnkh.taxi.web;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.service.DispatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.context.result.ResultFactory;

@Tag(name = "司机(车辆)操作")
@Path("/taxi")
public class TaxiController {
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private DispatchService dispatchService;

	@Operation(description = "车辆抢单")
	@POST
	@Path("/grab_order")
	public Result grabOrder(@RequestBody GrabOrderRequest request) {
		dispatchService.grabOrder(request);
		return resultFactory.success();
	}
	
	@Operation(description = "确认接单")
	@POST
	@Path("confirm_order")
	public Result confirm_order(@RequestBody GrabOrderRequest request) {
		boolean success = dispatchService.confirmOrder(request);
		if(success) {
			return resultFactory.success();
		}
		return resultFactory.error("确认失败");
	}
}
