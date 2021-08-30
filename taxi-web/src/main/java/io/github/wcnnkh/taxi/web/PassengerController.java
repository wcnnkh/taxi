package io.github.wcnnkh.taxi.web;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
	public DataResult<Order> postOrder(@RequestBody PostOrderRequest request) {
		Order order = dispatchService.postOrder(request);
		return resultFactory.success(order);
	};

	@Operation(description = "获取乘客附近车辆")
	@GET
	@POST
	@Path("nearby_taxi")
	public DataResult<List<Taxi>> getNearbyTaxi(@RequestBody NearbyTaxiQuery query) {
		List<Taxi> list = taxiService.getNearbyTaxis(query);
		return resultFactory.success(list);
	}
	
	@GET
	@Path("/orders")
	public DataResult<Page<Order>> getOrders(String passengerId, long pageNumber, long limit){
		return resultFactory.success(orderService.getPassengerOrders(passengerId, pageNumber, limit));
	}
}
