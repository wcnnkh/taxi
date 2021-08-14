package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import scw.util.page.Page;

public interface OrderService {
	Order getOrder(String orderId);
	
	/**
	 * 录单，此方法不触发事件
	 * @param request
	 * @return
	 */
	Order record(PostOrderRequest request);
	
	/**
	 * 修改订单状态,此方法实现不发送状态变更通知
	 * @see OrderStatusEventDispatcher#publishEvent(io.github.wcnnkh.taxi.core.event.OrderStatusEvent)
	 * @param request
	 * @param status
	 * @return
	 */
	boolean updateStatus(GrabOrderRequest request, OrderStatus status);
	
	Page<Order> getPassengerOrders(String passengerId, long pageNumber, long limit);
	
	Page<Order> getTaxiOrders(String taxiId, long pageNumber, long limit);
}
