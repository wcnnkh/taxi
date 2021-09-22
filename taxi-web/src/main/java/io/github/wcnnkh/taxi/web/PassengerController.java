package io.github.wcnnkh.taxi.web;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import io.basc.framework.beans.annotation.Autowired;
import io.basc.framework.context.result.DataResult;
import io.basc.framework.context.result.ResultFactory;
import io.basc.framework.util.page.Page;
import io.basc.framework.web.message.annotation.RequestBody;
import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.service.DispatchService;
import io.github.wcnnkh.taxi.core.service.OrderService;
import io.github.wcnnkh.taxi.core.service.TaxiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "乘客操作")
@Path("/passenger")
public class PassengerController {
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private DispatchService dispatchService;
	@Autowired
	private TaxiService taxiService;
	@Autowired
	private OrderService orderService;

	@Operation(description = "下单")
	@POST
	@Path("/post_order")
	public DataResult<Order> postOrder(@RequestBody @Valid @BeanParam PostOrderRequest request) {
		return dispatchService.postOrder(request);
	};

	@Operation(description = "获取乘客附近车辆")
	@POST
	@Path("nearby_taxi")
	public DataResult<List<Taxi>> getNearbyTaxi(@Valid @NotNull @RequestBody NearbyTaxiQuery query) {
		List<Taxi> list = taxiService.getNearbyTaxis(query);
		return resultFactory.success(list);
	}

	@Operation(description = "获取乘客历史订单")
	@GET
	@Path("/orders")
	public DataResult<Page<Order>> getOrders(
			@Parameter(description = "乘客id", required = true) @QueryParam("passengerId") String passengerId,
			@Parameter(description = "页码") @QueryParam("pageNumber") long pageNumber,
			@Parameter(description = "数量") @QueryParam("limit") @DefaultValue("10") long limit) {
		return resultFactory.success(orderService.getPassengerOrders(passengerId, pageNumber, limit));
	}
}
