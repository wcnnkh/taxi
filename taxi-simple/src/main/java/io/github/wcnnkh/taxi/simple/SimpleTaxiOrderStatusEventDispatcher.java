package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.TaxiOrderStatusEventDispatcher;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.EventDispatcher;
import scw.event.support.DefaultAsyncEventDispatcher;
import scw.event.support.DefaultNamedEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleTaxiOrderStatusEventDispatcher extends DefaultNamedEventDispatcher<String, OrderStatusEvent>
		implements TaxiOrderStatusEventDispatcher {

	public SimpleTaxiOrderStatusEventDispatcher() {
		super(true);
	}

	@Override
	protected EventDispatcher<OrderStatusEvent> createEventDispatcher(String name) {
		return new DefaultAsyncEventDispatcher<>();
	}
}
