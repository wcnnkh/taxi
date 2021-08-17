package io.github.wcnnkh.taxi.core.event.listener;

import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.ConfirmTimeoutEventDispatcher;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.OrderService;
import io.github.wcnnkh.taxi.core.service.impl.DispatchServiceImpl;

import java.util.concurrent.TimeUnit;

import scw.event.EventListener;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mapper.Copy;

public class GrabOrderEventListener implements EventListener<GrabOrderEvent> {
	private static Logger logger = LoggerFactory.getLogger(DispatchServiceImpl.class);
	private final OrderService orderService;
	private final OrderStatusEventDispatcher orderStatusEventDispatcher;
	private final ConfirmTimeoutEventDispatcher confirmTimeoutEventDispatcher;

	public GrabOrderEventListener(OrderService orderService, OrderStatusEventDispatcher orderStatusEventDispatcher, ConfirmTimeoutEventDispatcher confirmTimeoutEventDispatcher) {
		this.orderService = orderService;
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
		this.confirmTimeoutEventDispatcher = confirmTimeoutEventDispatcher;
	}

	@Override
	public void onEvent(GrabOrderEvent event) {
		while (true) {
			Order order = orderService.getOrder(event.getGrabOrderRequest().getOrderId());
			if (order == null) {
				// 订单不存在
				break;
			}
			
			if(!OrderStatus.canGrabOrder(order.getStatus())) {
				//订单状态不对，结束调度
				break;
			}

			if (isDispatchTimeout(order)) {
				// 调度超时
				if(orderService.updateStatus(event.getGrabOrderRequest(), OrderStatus.NO_SUPPLY)) {
					Order newOrder = new Order();
					Copy.copy(newOrder, order);
					newOrder.setStatus(OrderStatus.NO_SUPPLY.getCode());
					logger.info("发送无供事件:{}", event.getGrabOrderRequest().getOrderId());
					orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order, newOrder));
				}
				break;
			}

			if(orderService.updateStatus(event.getGrabOrderRequest(), OrderStatus.PRE_CONFIRM)){
				logger.info("预确认司机：" + event);
				Order newOrder = new Order();
				Copy.copy(newOrder, order);
				newOrder.setTaxiId(event.getGrabOrderRequest().getTaxiId());
				newOrder.setStatus(OrderStatus.PRE_CONFIRM.getCode());
				orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(order, newOrder));
				
				//发送一个延迟事件，30s后判断司机是否确认接单，如果没有就当作超时
				confirmTimeoutEventDispatcher.publishEvent(event, 30, TimeUnit.SECONDS);
				break;
			}
			
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	protected boolean isDispatchTimeout(Order order) {
		return System.currentTimeMillis() - order.getCreateTime() > order.getDispatchTimeout();
	}
}
