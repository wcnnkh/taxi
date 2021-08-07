package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.service.OrderService;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.db.DB;
import scw.sql.SimpleSql;
import scw.sql.Sql;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class OrderServiceImpl implements OrderService{
	private final DB db;
	
	public OrderServiceImpl(DB db) {
		this.db = db;
	}
	
	@Override
	public Order getOrder(String orderId) {
		return db.getById(Order.class, orderId);
	}

	@Override
	public boolean bind(GrabOrderRequest request) {
		Sql sql = new SimpleSql("update set `order` status=?, taxiId = ? where orderId = ?", OrderStatus.CONFIRM.getCode(), request.getTaxiId(), request.getOrderId());
		return db.update(sql) > 0;
	}

}
