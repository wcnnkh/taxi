package io.github.wcnnkh.taxi.core.event.listener;

import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.service.OrderService;
import scw.event.EventListener;

public class ConfirmTimeoutEventListener implements EventListener<GrabOrderEvent>{
	private OrderService orderService;
	
	public ConfirmTimeoutEventListener(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@Override
	public void onEvent(GrabOrderEvent event) {
		orderService.updateStatus(event.getGrabOrderRequest(), OrderStatus.NO_SUPPLY);
	}

}
