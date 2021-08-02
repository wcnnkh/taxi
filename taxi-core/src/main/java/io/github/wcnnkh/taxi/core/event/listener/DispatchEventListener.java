package io.github.wcnnkh.taxi.core.event.listener;

import java.util.List;

import io.github.wcnnkh.taxi.core.dto.Location;
import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.TaxiOrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.TaxiService;
import scw.core.utils.StringUtils;
import scw.event.EventListener;

public class DispatchEventListener implements EventListener<OrderStatusEvent> {
	private final TaxiService taxiService;
	private final TaxiOrderStatusEventDispatcher taxiOrderStatusEventDispatcher;

	public DispatchEventListener(TaxiService taxiService,
			TaxiOrderStatusEventDispatcher taxiOrderStatusEventDispatcher) {
		this.taxiService = taxiService;
		this.taxiOrderStatusEventDispatcher = taxiOrderStatusEventDispatcher;
	}

	@Override
	public void onEvent(OrderStatusEvent event) {
		if (OrderStatus.RECORD.getCode().equals(event.getOrder().getStatus())) {
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
				for (Taxi taxi : taxis) {
					taxiOrderStatusEventDispatcher.publishEvent(taxi.getId(), event);
				}
			}
		}

		if (StringUtils.isNotEmpty(event.getOrder().getTaxiId())) {
			taxiOrderStatusEventDispatcher.publishEvent(event.getOrder().getTaxiId(), event);
		}
	}

}
