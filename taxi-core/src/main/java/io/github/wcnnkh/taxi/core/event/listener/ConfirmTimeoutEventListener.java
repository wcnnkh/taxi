package io.github.wcnnkh.taxi.core.event.listener;

import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.service.OrderService;
import scw.event.EventListener;
import scw.logger.Logger;
import scw.logger.LoggerFactory;

public class ConfirmTimeoutEventListener implements EventListener<GrabOrderEvent>{
	private static Logger logger = LoggerFactory.getLogger(ConfirmTimeoutEventListener.class);
	
	private OrderService orderService;
	
	public ConfirmTimeoutEventListener(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@Override
	public void onEvent(GrabOrderEvent event) {
		if(orderService.updateStatus(event.getGrabOrderRequest(), OrderStatus.NO_SUPPLY)){
			logger.info("司机确认接单超时：{}", event);
		}
	}

}
