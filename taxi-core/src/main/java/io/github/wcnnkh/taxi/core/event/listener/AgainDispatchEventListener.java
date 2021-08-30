package io.github.wcnnkh.taxi.core.event.listener;

import io.basc.framework.event.EventListener;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.logger.Logger;
import io.basc.framework.logger.LoggerFactory;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.DispatchEventDispatcher;
import io.github.wcnnkh.taxi.core.service.OrderService;

public class AgainDispatchEventListener implements
		EventListener<ObjectEvent<Order>> {
	private static Logger logger = LoggerFactory
			.getLogger(AgainDispatchEventListener.class);
	private final OrderService orderService;
	private final DispatchEventDispatcher dispatchEventDispatcher;

	public AgainDispatchEventListener(OrderService orderService,
			DispatchEventDispatcher dispatchEventDispatcher) {
		this.orderService = orderService;
		this.dispatchEventDispatcher = dispatchEventDispatcher;
	}

	@Override
	public void onEvent(ObjectEvent<Order> event) {
		UpdateOrderStatusRequest resetStatusRequest = new UpdateOrderStatusRequest(
				event.getSource().getId(), OrderStatus.RECORD);
		resetStatusRequest.setTaxiId(event.getSource().getTaxiId());
		if (orderService.updateStatus(resetStatusRequest)) {
			// 重新调度
			Order order = orderService.getOrder(event.getSource().getId());
			if (order == null) {
				return;
			}

			logger.info("开始重新调度：{}", order);
			dispatchEventDispatcher.publishEvent(new ObjectEvent<Order>(order));
		}
	}

}
