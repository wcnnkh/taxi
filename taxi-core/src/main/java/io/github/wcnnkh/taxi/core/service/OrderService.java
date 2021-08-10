package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;

public interface OrderService {
	Order getOrder(String orderId);
	
	/**
	 * 修改订单状态,此方法实现不发送状态变更通知
	 * @see OrderStatusEventDispatcher#publishEvent(io.github.wcnnkh.taxi.core.event.OrderStatusEvent)
	 * @param request
	 * @param status
	 * @return
	 */
	boolean updateStatus(GrabOrderRequest request, OrderStatus status);
}
