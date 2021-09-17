package io.github.wcnnkh.taxi.core.event.listener;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.basc.framework.event.EventListener;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.logger.Logger;
import io.basc.framework.logger.LoggerFactory;
import io.basc.framework.mapper.Copy;
import io.basc.framework.util.StringUtils;
import io.github.wcnnkh.taxi.core.dto.Location;
import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.DispatchEventDispatcher;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.DispatchPolicyService;
import io.github.wcnnkh.taxi.core.service.OrderService;
import io.github.wcnnkh.taxi.core.service.TaxiService;

/**
 * 通知司机抢单
 * 
 * @author shuchaowen
 *
 */
public class DispatchEventListener implements EventListener<ObjectEvent<Order>> {
	private static Logger logger = LoggerFactory.getLogger(DispatchEventListener.class);
	private final TaxiService taxiService;
	private final OrderStatusEventDispatcher orderStatusEventDispatcher;
	private final DispatchPolicyService dispatchPolicyService;
	private final OrderService orderService;
	private final DispatchEventDispatcher dispatcherEventDispatcher;

	public DispatchEventListener(TaxiService taxiService, OrderStatusEventDispatcher orderStatusEventDispatcher,
			DispatchPolicyService dispatchPolicyService, OrderService orderService,
			DispatchEventDispatcher dispatcherEventDispatcher) {
		this.taxiService = taxiService;
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
		this.dispatchPolicyService = dispatchPolicyService;
		this.orderService = orderService;
		this.dispatcherEventDispatcher = dispatcherEventDispatcher;
	}

	public void dispatch(Order dispathOrder) {
		Location location = dispathOrder.getStartLocation();
		if (location == null) {
			return;
		}

		NearbyTaxiQuery nearbyTaxiQuery = new NearbyTaxiQuery();
		nearbyTaxiQuery.setLocation(location);
		TaxiStatus taxiStatus = new TaxiStatus();
		taxiStatus.setWorking(true);
		taxiStatus.setOperation(false);
		nearbyTaxiQuery.setTaxiStatus(taxiStatus);
		List<Taxi> taxis = taxiService.getNearbyTaxis(nearbyTaxiQuery);
		
		if(logger.isDebugEnabled()) {
			logger.debug("订单{}调度找到的司机列表:{}", dispathOrder.getId(),
					taxis.stream().map((m) -> m.getId()).collect(Collectors.toList()));
		}
		
		for (Taxi taxi : taxis) {
			if (!dispatchPolicyService.canNotifyGrab(dispathOrder.getId(), taxi.getId())) {
				continue;
			}

			if(logger.isTraceEnabled()) {
				logger.trace("推送给司机[{}]进行抢单:{}", taxi.getId(), dispathOrder.getId());
			}
		
			Order order = new Order();
			Copy.copy(dispathOrder, order);
			order.setTaxiId(taxi.getId());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(dispathOrder, order));
		}
	}

	@Override
	public void onEvent(ObjectEvent<Order> event) {
		if(logger.isTraceEnabled()) {
			logger.trace("收到下单事件，开始调度：{}", event);
		}
		
		Order dispatchOrder = orderService.getOrder(event.getSource().getId());
		if (StringUtils.isNotEmpty(dispatchOrder.getTaxiId())) {
			return;
		}

		if (!OrderStatus.RECORD.getCode().equals(dispatchOrder.getStatus())) {
			return;
		}

		if (dispatchPolicyService.isTimeout(dispatchOrder)) {
			// 超时
			UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
			request.setOrderId(dispatchOrder.getId());
			request.setStatus(OrderStatus.NO_SUPPLY);
			if (orderService.updateStatus(request)) {
				Order newOrder = new Order();
				Copy.copy(dispatchOrder, newOrder);
				newOrder.setStatus(OrderStatus.NO_SUPPLY.getCode());
				logger.info("发送无供事件:{}", dispatchOrder.getId());
				orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(dispatchOrder, newOrder));
			}
			return ;
		}

		dispatch(dispatchOrder);

		// 发送一个延迟消息，再次调度，存在终结条件，如状态不对或调度超时
		// 10秒后再次调度找车
		dispatcherEventDispatcher.publishEvent(event, 10, TimeUnit.SECONDS);
	}
}
