package io.github.wcnnkh.taxi.web;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import io.basc.framework.beans.annotation.Autowired;
import io.basc.framework.context.result.DataResult;
import io.basc.framework.context.result.Result;
import io.basc.framework.context.result.ResultFactory;
import io.basc.framework.util.page.Page;
import io.basc.framework.web.message.annotation.RequestBody;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.TaxiOrderRequest;
import io.github.wcnnkh.taxi.core.service.DispatchService;
import io.github.wcnnkh.taxi.core.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "司机(车辆)操作")
@Path("/taxi")
public class TaxiController {
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private DispatchService dispatchService;
	@Autowired
	private OrderService orderService;

	@Operation(description = "车辆抢单")
	@POST
	@Path("/grab_order")
	public Result grabOrder(@RequestBody @Valid TaxiOrderRequest request) {
		dispatchService.grabOrder(request);
		return resultFactory.success();
	}
	
	@Operation(description = "确认接单")
	@POST
	@Path("confirm_order")
	public Result confirm_order(@RequestBody @Valid TaxiOrderRequest request) {
		boolean success = dispatchService.confirmOrder(request);
		if(success) {
			return resultFactory.success();
		}
		return resultFactory.error("确认失败");
	}
	
	@GET
	@Path("/orders")
	public DataResult<Page<Order>> getOrders(String taxiId, long pageNumber, long limit){
		return resultFactory.success(orderService.getTaxiOrders(taxiId, pageNumber, limit));
	}

	@POST
	@Path("/arrive")
	public Result arrive(@RequestBody @Valid TaxiOrderRequest request) {
		boolean success = dispatchService.arrive(request);
		if(success) {
			return resultFactory.success();
		}
		return resultFactory.error();
	}

	@POST
	@Path("/receive_passenger")
	public Result receivePassenger(@RequestBody @Valid  TaxiOrderRequest request) {
		boolean success = dispatchService.receivePassenger(request);
		if(success) {
			return resultFactory.success();
		}
		return resultFactory.error();
	}
}
