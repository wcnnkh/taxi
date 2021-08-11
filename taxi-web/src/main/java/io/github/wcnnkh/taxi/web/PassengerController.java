package io.github.wcnnkh.taxi.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.service.DispatchService;
import io.github.wcnnkh.taxi.core.service.TaxiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import scw.beans.annotation.Autowired;
import scw.context.result.DataResult;
import scw.context.result.ResultFactory;

@Tag(name = "乘客操作")
@Path("/passenger")
public class PassengerController {
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private DispatchService dispatchService;
	@Autowired
	private TaxiService taxiService;

	@Operation(description = "下单")
	@POST
	@Path("/post_order")
	public DataResult<Order> postOrder(@scw.web.message.annotation.RequestBody PostOrderRequest request) {
		Order order = dispatchService.postOrder(request);
		return resultFactory.success(order);
	};

	@Operation(description = "获取乘客附近车辆")
	@GET
	@Path("nearby_taxi")
	public DataResult<List<Taxi>> getNearbyTaxi(@RequestBody NearbyTaxiQuery query) {
		List<Taxi> list = taxiService.getNearbyTaxis(query);
		return resultFactory.success(list);
	}
}
