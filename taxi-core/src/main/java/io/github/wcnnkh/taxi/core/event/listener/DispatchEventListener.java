package io.github.wcnnkh.taxi.core.event.listener;

import java.util.List;
import java.util.stream.Collectors;

import io.github.wcnnkh.taxi.core.dto.Location;
import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.TaxiService;
import scw.core.utils.StringUtils;
import scw.event.EventListener;
import scw.event.ObjectEvent;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mapper.Copy;

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

	public DispatchEventListener(TaxiService taxiService, OrderStatusEventDispatcher orderStatusEventDispatcher) {
		this.taxiService = taxiService;
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
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
		logger.info("订单{}需要通知的司机列表:{}", dispathOrder.getId(),
				taxis.stream().map((m) -> m.getId()).collect(Collectors.toList()));
		for (Taxi taxi : taxis) {
			Order order = new Order();
			Copy.copy(order, dispathOrder);
			order.setTaxiId(taxi.getId());
			orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(dispathOrder, order));
		}
	}

	@Override
	public void onEvent(ObjectEvent<Order> event) {
		logger.info("收到下单事件，开始调度：{}", event);
		Order dispatchOrder = event.getSource();
		if (StringUtils.isNotEmpty(dispatchOrder.getTaxiId())) {
			return;
		}

		if (!OrderStatus.RECORD.getCode().equals(dispatchOrder.getStatus())) {
			return;
		}
		
		//TODO 后面优化，现在是只找一次车，找不到就算了
		dispatch(event.getSource());
	}
}
