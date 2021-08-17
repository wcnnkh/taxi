package io.github.wcnnkh.taxi.core.event.listener;

import io.github.wcnnkh.taxi.core.dto.Location;
import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.TaxiService;

import java.util.List;
import java.util.stream.Collectors;

import scw.core.utils.StringUtils;
import scw.event.EventListener;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mapper.Copy;

/**
 * 通知司机抢单
 * @author shuchaowen
 *
 */
public class DispatchEventListener implements EventListener<OrderStatusEvent> {
	private static Logger logger = LoggerFactory.getLogger(DispatchEventListener.class);
	private final TaxiService taxiService;
	private final OrderStatusEventDispatcher orderStatusEventDispatcher;

	public DispatchEventListener(TaxiService taxiService,
			OrderStatusEventDispatcher orderStatusEventDispatcher) {
		this.taxiService = taxiService;
		this.orderStatusEventDispatcher = orderStatusEventDispatcher;
	}

	@Override
	public void onEvent(OrderStatusEvent event) {
		if (StringUtils.isEmpty(event.getOrder().getTaxiId()) && OrderStatus.RECORD.getCode().equals(event.getOrder().getStatus())) {
			logger.info("收到下单事件，开始调度：{}", event);
			// 录单，开始发送订单通知给司机让司机抢单
			Location location = event.getOrder().getStartLocation();
			if (location != null) {
				NearbyTaxiQuery nearbyTaxiQuery = new NearbyTaxiQuery();
				nearbyTaxiQuery.setLocation(location);
				TaxiStatus taxiStatus = new TaxiStatus();
				taxiStatus.setWorking(true);
				taxiStatus.setOperation(false);
				nearbyTaxiQuery.setTaxiStatus(taxiStatus);
				List<Taxi> taxis = taxiService.getNearbyTaxis(nearbyTaxiQuery);
				logger.info("订单{}需要通知的司机列表:{}", event.getOrder().getId(), taxis.stream().map((m) -> m.getId()).collect(Collectors.toList()));
				for (Taxi taxi : taxis) {
					Order order = new Order();
					Copy.copy(order, event.getOrder());
					order.setTaxiId(taxi.getId());
					orderStatusEventDispatcher.publishEvent(new OrderStatusEvent(event.getOrder(), order));
				}
			}
		}
	}

}
