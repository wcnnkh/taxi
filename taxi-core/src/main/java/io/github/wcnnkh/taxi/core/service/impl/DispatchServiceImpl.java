package io.github.wcnnkh.taxi.core.service.impl;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.mapper.Copy;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PassengerOrderRequest;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.dto.TaxiOrderRequest;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.AgainDispatchEventDispatcher;
import io.github.wcnnkh.taxi.core.event.ConfirmTimeoutEventDispatcher;
import io.github.wcnnkh.taxi.core.event.DispatchEventDispatcher;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.event.GrabOrderEventDispatcher;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.event.listener.ConfirmTimeoutEventListener;
import io.github.wcnnkh.taxi.core.event.listener.DispatchEventListener;
import io.github.wcnnkh.taxi.core.event.listener.GrabOrderEventListener;
import io.github.wcnnkh.taxi.core.service.DispatchPolicyService;
import io.github.wcnnkh.taxi.core.service.DispatchService;
import io.github.wcnnkh.taxi.core.service.OrderService;
import io.github.wcnnkh.taxi.core.service.TaxiService;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class DispatchServiceImpl implements DispatchService {
	private final OrderStatusEventDispatcher orderStatusEventDispatcher;
	private final GrabOrderEventDispatcher grabOrderEventDispatcher;
	private long defaultDispatchTime = 60000;
	private final OrderService orderService;
	private final DispatchEventDispatcher dispatchEventDispatcher;

	public DispatchServiceImpl(
			OrderStatusEventDispatcher orderStatusEventDispatcher,
			DispatchEventDispatcher dispatchEventDispatcher,
			GrabOrderEventDispatcher grabOrderEventDispatcher,
			OrderService orderService, TaxiService taxiService,
			ConfirmTimeoutEventDispatcher confirmTimeoutEventDispatcher,
			DispatchPolicyService dispatchPolicyService,
			AgainDispatchEventDispatcher againDispatchEventDispatcher) {
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
		this.grabOrderEventDispatcher = grabOrderEventDispatcher;
		this.orderService = orderService;
		this.dispatchEventDispatcher = dispatchEventDispatcher;
		grabOrderEventDispatcher.registerListener(new GrabOrderEventListener(
				orderService, orderStatusEventDispatcher,
				confirmTimeoutEventDispatcher, dispatchPolicyService));
		dispatchEventDispatcher.registerListener(new DispatchEventListener(
				taxiService, orderStatusEventDispatcher, dispatchPolicyService,
				orderService, dispatchEventDispatcher));
		confirmTimeoutEventDispatcher
				.registerListener(new ConfirmTimeoutEventListener(orderService,
						againDispatchEventDispatcher, dispatchEventDispatcher));
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
		orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(null,
				order));
		// 录单，开始发送订单通知给司机让司机抢单
		dispatchEventDispatcher.publishEvent(new ObjectEvent<>(order));
		return order;
	}

	@Override
	public void grabOrder(TaxiOrderRequest request) {
		grabOrderEventDispatcher.publishEvent(new GrabOrderEvent(request));
	}

	@Override
	public boolean confirmOrder(TaxiOrderRequest request) {
		Order order = orderService.getOrder(request.getOrderId());
		if (order == null) {
			return false;
		}

		UpdateOrderStatusRequest updateStatusRequest = new UpdateOrderStatusRequest();
		updateStatusRequest.setOrderId(request.getOrderId());
		updateStatusRequest.setTaxiId(request.getTaxiId());
		updateStatusRequest.setStatus(OrderStatus.CONFIRM);
		if (orderService.updateStatus(updateStatusRequest)) {
			Order newOrder = new Order();
			Copy.copy(order, newOrder);
			newOrder.setStatus(OrderStatus.CONFIRM.getCode());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order,
					newOrder));
			return true;
		}
		return false;
	}

	@Override
	public boolean receivePassenger(TaxiOrderRequest request) {
		Order order = orderService.getOrder(request.getOrderId());
		if (order == null) {
			return false;
		}

		UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest();
		updateOrderStatusRequest.setOrderId(request.getOrderId());
		updateOrderStatusRequest.setTaxiId(request.getTaxiId());
		updateOrderStatusRequest.setStatus(OrderStatus.RECEIVE_PASSENGER);
		boolean success = orderService.updateStatus(updateOrderStatusRequest);
		if (success) {
			Order newOrder = Copy.clone(order);
			newOrder.setStatus(OrderStatus.RECEIVE_PASSENGER.getCode());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order,
					newOrder));
		}
		return success;
	}

	@Override
	public boolean arrive(TaxiOrderRequest request) {
		Order order = orderService.getOrder(request.getOrderId());
		if (order == null) {
			return false;
		}

		UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest();
		updateOrderStatusRequest.setOrderId(request.getOrderId());
		updateOrderStatusRequest.setTaxiId(request.getTaxiId());
		updateOrderStatusRequest.setStatus(OrderStatus.ARRIVE);
		boolean success = orderService.updateStatus(updateOrderStatusRequest);
		if (success) {
			Order newOrder = Copy.clone(order);
			newOrder.setStatus(OrderStatus.ARRIVE.getCode());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order,
					newOrder));
		}
		return success;
	}

	@Override
	public boolean cancel(TaxiOrderRequest request) {
		Order order = orderService.getOrder(request.getOrderId());
		if (order == null) {
			return false;
		}

		UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest();
		updateOrderStatusRequest.setOrderId(request.getOrderId());
		updateOrderStatusRequest.setTaxiId(request.getTaxiId());
		updateOrderStatusRequest.setStatus(OrderStatus.TAXI_CANCEL);
		boolean success = orderService.updateStatus(updateOrderStatusRequest);
		if (success) {
			Order newOrder = Copy.clone(order);
			newOrder.setStatus(OrderStatus.TAXI_CANCEL.getCode());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order,
					newOrder));
		}
		return success;
	}

	@Override
	public boolean cancel(PassengerOrderRequest request) {
		Order order = orderService.getOrder(request.getOrderId());
		if (order == null) {
			return false;
		}

		UpdateOrderStatusRequest updateOrderStatusRequest = new UpdateOrderStatusRequest();
		updateOrderStatusRequest.setOrderId(request.getOrderId());
		updateOrderStatusRequest.setPassengerId(request.getPassengerId());
		updateOrderStatusRequest.setStatus(OrderStatus.PASSENGER_CANCEL);
		boolean success = orderService.updateStatus(updateOrderStatusRequest);
		if (success) {
			Order newOrder = Copy.clone(order);
			newOrder.setStatus(OrderStatus.PASSENGER_CANCEL.getCode());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order,
					newOrder));
		}
		return success;
	}
}
