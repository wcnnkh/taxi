package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.TaxiOrderStatusEventDispatcher;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.EventDispatcher;
import scw.event.support.SimpleAsyncEventDispatcher;
import scw.event.support.SimpleNamedEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleTaxiOrderStatusEventDispatcher extends SimpleNamedEventDispatcher<String, OrderStatusEvent>
		implements TaxiOrderStatusEventDispatcher {

	public SimpleTaxiOrderStatusEventDispatcher() {
		super(true);
	}

	@Override
	protected EventDispatcher<OrderStatusEvent> createEventDispatcher(String name) {
		return new SimpleAsyncEventDispatcher<>();
	}
}
