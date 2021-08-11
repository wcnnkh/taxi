package io.github.wcnnkh.taxi.core.service.impl;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.ConfirmTimeoutEventDispatcher;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.event.GrabOrderEventDispatcher;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.event.listener.ConfirmTimeoutEventListener;
import io.github.wcnnkh.taxi.core.event.listener.DispatchEventListener;
import io.github.wcnnkh.taxi.core.event.listener.GrabOrderEventListener;
import io.github.wcnnkh.taxi.core.service.DispatchService;
import io.github.wcnnkh.taxi.core.service.OrderService;
import io.github.wcnnkh.taxi.core.service.TaxiService;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.mapper.Copy;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class DispatchServiceImpl implements DispatchService {
	private final OrderStatusEventDispatcher orderStatusEventDispatcher;
	private final GrabOrderEventDispatcher grabOrderEventDispatcher;
	private long defaultDispatchTime = 60000;
	private final OrderService orderService;

	public DispatchServiceImpl(OrderStatusEventDispatcher orderStatusEventDispatcher,
			GrabOrderEventDispatcher grabOrderEventDispatcher, OrderService orderService, TaxiService taxiService,
			ConfirmTimeoutEventDispatcher confirmTimeoutEventDispatcher) {
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
		this.grabOrderEventDispatcher = grabOrderEventDispatcher;
		this.orderService = orderService;
		grabOrderEventDispatcher.registerListener(
				new GrabOrderEventListener(orderService, orderStatusEventDispatcher, confirmTimeoutEventDispatcher));
		orderStatusEventDispatcher
				.registerListener(new DispatchEventListener(taxiService, orderStatusEventDispatcher));
		confirmTimeoutEventDispatcher.registerListener(new ConfirmTimeoutEventListener(orderService));
	}

	public long getDefaultDispatchTime() {
		return defaultDispatchTime;
	}

	public void setDefaultDispatchTime(long defaultDispatchTime) {
		this.defaultDispatchTime = defaultDispatchTime;
	}

	@Override
	public Order postOrder(PostOrderRequest request) {
		if (request.getDispatchTimeout() == null) {
			request.setDispatchTimeout(getDefaultDispatchTime());
		}
		
		Order order = orderService.record(request);
		orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(null, order));
		return order;
	}

	@Override
	public void grabOrder(GrabOrderRequest request) {
		grabOrderEventDispatcher.publishEvent(new GrabOrderEvent(request));
	}

	@Override
	public boolean confirmOrder(GrabOrderRequest request) {
		Order order = orderService.getOrder(request.getOrderId());
		if(order == null) {
			return false;
		}
		if(orderService.updateStatus(request, OrderStatus.CONFIRM)){
			Order newOrder = new Order();
			Copy.copy(newOrder, order);
			newOrder.setStatus(OrderStatus.CONFIRM.getCode());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order, newOrder));
			return true;
		}
		return false;
	}
}
