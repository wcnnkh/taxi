package io.github.wcnnkh.taxi.core.event.listener;

import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.AgainDispatchEventDispatcher;
import io.github.wcnnkh.taxi.core.event.DispatchEventDispatcher;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.service.OrderService;

import java.util.concurrent.TimeUnit;

import scw.event.EventListener;
import scw.event.ObjectEvent;
import scw.logger.Logger;
import scw.logger.LoggerFactory;

public class ConfirmTimeoutEventListener implements
		EventListener<GrabOrderEvent> {
	private static Logger logger = LoggerFactory
			.getLogger(ConfirmTimeoutEventListener.class);
	private OrderService orderService;
	private AgainDispatchEventDispatcher againDispatchEventDispatcher;

	public ConfirmTimeoutEventListener(OrderService orderService,
			AgainDispatchEventDispatcher againDispatchEventDispatcher,
			DispatchEventDispatcher dispatchEventDispatcher) {
		this.orderService = orderService;
		this.againDispatchEventDispatcher = againDispatchEventDispatcher;
		againDispatchEventDispatcher
				.registerListener(new AgainDispatchEventListener(orderService,
						dispatchEventDispatcher));
	}

	@Override
	public void onEvent(GrabOrderEvent event) {
		UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(event
				.getGrabOrderRequest().getOrderId(),
				OrderStatus.CONFIRM_TIMEOUT);
		request.setTaxiId(event.getGrabOrderRequest().getTaxiId());
		if (orderService.updateStatus(request)) {
			logger.info("司机确认接单超时稍后重新调度此订单：{}", event);

			Order order = orderService.getOrder(event.getGrabOrderRequest()
					.getOrderId());
			if (order == null) {
				return;
			}

			// 30秒后尝试重新调度
			againDispatchEventDispatcher.publishEvent(new ObjectEvent<Order>(
					order), 30, TimeUnit.SECONDS);
		}
	}

}
