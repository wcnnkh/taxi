package io.github.wcnnkh.taxi.core.service.impl;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.event.ConfirmTimeoutEventDispatcher;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.event.GrabOrderEventDispatcher;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.event.TaxiOrderStatusEventDispatcher;
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

	public DispatchServiceImpl(OrderStatusEventDispatcher orderStatusEventDispatcher,
			GrabOrderEventDispatcher grabOrderEventDispatcher, OrderService orderService, TaxiService taxiService,
			TaxiOrderStatusEventDispatcher taxiOrderStatusEventDispatcher,
			ConfirmTimeoutEventDispatcher confirmTimeoutEventDispatcher) {
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
		this.grabOrderEventDispatcher = grabOrderEventDispatcher;
		grabOrderEventDispatcher.registerListener(
				new GrabOrderEventListener(orderService, orderStatusEventDispatcher, confirmTimeoutEventDispatcher));
		orderStatusEventDispatcher
				.registerListener(new DispatchEventListener(taxiService, taxiOrderStatusEventDispatcher));
		confirmTimeoutEventDispatcher.registerListener(new ConfirmTimeoutEventListener(orderService));
	}

	public long getDefaultDispatchTime() {
		return defaultDispatchTime;
	}

	public void setDefaultDispatchTime(long defaultDispatchTime) {
		this.defaultDispatchTime = defaultDispatchTime;
	}

	@Override
	public void postOrder(PostOrderRequest request) {
		Order order = new Order();
		Copy.copy(order, request);

		if (request.getDispatchTimeout() == null) {
			request.setDispatchTimeout(getDefaultDispatchTime());
		}

		if (order.getCreateTime() == null) {
			order.setCreateTime(System.currentTimeMillis());
		}
		orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(null, order));
	}

	@Override
	public void grabOrder(GrabOrderRequest request) {
		grabOrderEventDispatcher.publishEvent(new GrabOrderEvent(request));
	}
}
