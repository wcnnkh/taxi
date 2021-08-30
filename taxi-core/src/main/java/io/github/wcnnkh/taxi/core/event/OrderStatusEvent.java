package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.BasicEvent;
import io.basc.framework.mapper.MapperUtils;
import io.github.wcnnkh.taxi.core.dto.Order;

public class OrderStatusEvent extends BasicEvent {
	private static final long serialVersionUID = 1L;
	private final Order oldOrder;
	private final Order order;

	public OrderStatusEvent(Order oldOrder, Order order) {
		this.oldOrder = oldOrder;
		this.order = order;
	}

	public Order getOldOrder() {
		return oldOrder;
	}

	public Order getOrder() {
		return order;
	}
	
	@Override
	public String toString() {
		return MapperUtils.toString(this);
	}
}
