package io.github.wcnnkh.taxi.core.event.listener;

import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.OrderService;
import scw.event.EventListener;
import scw.mapper.Copy;

public class GrabOrderEventListener implements EventListener<GrabOrderEvent> {
	private final OrderService orderService;
	private final OrderStatusEventDispatcher orderStatusEventDispatcher;

	public GrabOrderEventListener(OrderService orderService, OrderStatusEventDispatcher orderStatusEventDispatcher) {
		this.orderService = orderService;
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
	}

	@Override
	public void onEvent(GrabOrderEvent event) {
		while (true) {
			Order order = orderService.getOrder(event.getGrabOrderRequest().getOrderId());
			if (order == null) {
				// 订单不存在
				break;
			}

			// TODO 判断订单状态
			if (System.currentTimeMillis() - order.getCreateTime() > order.getDispatchTimeout()) {
				// 调度超时
				Order newOrder = new Order();
				Copy.copy(newOrder, order);
				newOrder.setStatus(OrderStatus.NO_SUPPLY.getCode());
				orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order, newOrder));
				break;
			}

			if(!orderService.bind(event.getGrabOrderRequest())){
				// 绑定失败，尝试重新绑定
				continue;
			}

			Order newOrder = new Order();
			Copy.copy(newOrder, order);
			newOrder.setStatus(OrderStatus.PRE_CONFIRM.getCode());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order, newOrder));
			break;
		}
	}

}
