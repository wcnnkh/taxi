package io.github.wcnnkh.taxi.core.service;

import io.basc.framework.util.page.Page;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;

public interface OrderService {
	Order getOrder(String orderId);

	/**
	 * 录单，此方法不触发事件
	 * 
	 * @param request
	 * @return
	 */
	Order record(PostOrderRequest request);

	/**
	 * 修改订单状态,此方法实现不发送状态变更通知
	 * 
	 * @see OrderStatusEventDispatcher#publishEvent(io.github.wcnnkh.taxi.core.event.OrderStatusEvent)
	 * @param request
	 * @param status
	 * @return
	 */
	boolean updateStatus(UpdateOrderStatusRequest request);

	Page<Order> getPassengerOrders(String passengerId, long pageNumber, long limit);

	Page<Order> getTaxiOrders(String taxiId, long pageNumber, long limit);

	/**
	 * 车辆正在进行中的订单
	 * 
	 * @param taxiId
	 * @return
	 */
	Order getTaxiOrdersInProgress(String taxiId);

	/**
	 * 乘客正在进行中的订单
	 * 
	 * @param passengerId
	 * @return
	 */
	Order getPassengerOrdersInProgress(String passengerId);
}
