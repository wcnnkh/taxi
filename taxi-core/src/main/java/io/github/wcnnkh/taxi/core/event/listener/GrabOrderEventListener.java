package io.github.wcnnkh.taxi.core.event.listener;

import java.util.concurrent.TimeUnit;

import io.basc.framework.event.EventListener;
import io.basc.framework.logger.Logger;
import io.basc.framework.logger.LoggerFactory;
import io.basc.framework.mapper.Copy;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.ConfirmTimeoutEventDispatcher;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.DispatchPolicyService;
import io.github.wcnnkh.taxi.core.service.OrderService;
import io.github.wcnnkh.taxi.core.service.impl.DispatchServiceImpl;

public class GrabOrderEventListener implements EventListener<GrabOrderEvent> {
	private static Logger logger = LoggerFactory.getLogger(DispatchServiceImpl.class);
	private final OrderService orderService;
	private final OrderStatusEventDispatcher orderStatusEventDispatcher;
	private final ConfirmTimeoutEventDispatcher confirmTimeoutEventDispatcher;
	private final DispatchPolicyService dispatchPolicyService;

	public GrabOrderEventListener(OrderService orderService, OrderStatusEventDispatcher orderStatusEventDispatcher,
			ConfirmTimeoutEventDispatcher confirmTimeoutEventDispatcher, DispatchPolicyService dispatchPolicyService) {
		this.orderService = orderService;
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
		this.confirmTimeoutEventDispatcher = confirmTimeoutEventDispatcher;
		this.dispatchPolicyService = dispatchPolicyService;
	}

	@Override
	public void onEvent(GrabOrderEvent event) {
		Order order = orderService.getOrder(event.getGrabOrderRequest().getOrderId());
		if (order == null) {
			// 订单不存在
			return;
		}

		if (!OrderStatus.canGrabOrder(order.getStatus())) {
			// 订单状态不对
			return;
		}

		if (dispatchPolicyService.isTimeout(order)) {
			//已超时
			return;
		}

		UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
		request.setOrderId(event.getGrabOrderRequest().getOrderId());
		request.setTaxiId(event.getGrabOrderRequest().getTaxiId());
		request.setStatus(OrderStatus.PRE_CONFIRM);
		if (orderService.updateStatus(request)) {
			if(logger.isDebugEnabled()) {
				logger.debug("预确认司机：" + event);
			}
			
			Order newOrder = new Order();
			Copy.copy(order, newOrder);
			newOrder.setTaxiId(event.getGrabOrderRequest().getTaxiId());
			newOrder.setStatus(OrderStatus.PRE_CONFIRM.getCode());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order, newOrder));

			// 发送一个延迟事件，30s后判断司机是否确认接单，如果没有就当作超时
			confirmTimeoutEventDispatcher.publishEvent(event, 30, TimeUnit.SECONDS);
		}
	}
}
