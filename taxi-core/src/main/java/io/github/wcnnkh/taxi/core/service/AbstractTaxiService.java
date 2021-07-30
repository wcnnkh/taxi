package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.OrderEventDispatcher;

public abstract class AbstractTaxiService implements TaxiService {
	private final OrderEventDispatcher orderEventDispatcher;

	public AbstractTaxiService(OrderEventDispatcher orderEventDispatcher) {
		this.orderEventDispatcher = orderEventDispatcher;
	}
}
