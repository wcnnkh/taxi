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
public class OrderServiceImpl implements OrderService {
	private final DB db;

	public OrderServiceImpl(DB db) {
		this.db = db;
	}

	@Override
	public Order getOrder(String orderId) {
		return db.getById(Order.class, orderId);
	}

	@Override
	public boolean updateStatus(GrabOrderRequest request, OrderStatus status) {
		Sql sql = null;
		if (status == OrderStatus.PRE_CONFIRM) {
			// 预绑定
			sql = new SimpleSql("update set `order` status=?, taxiId = ? where orderId = ? and status=?",
					OrderStatus.PRE_CONFIRM.getCode(), request.getTaxiId(), request.getOrderId(), OrderStatus.RECORD);
		} else if (status == OrderStatus.CONFIRM_TIMEOUT) {
			sql = new SimpleSql("update set `order` status=? where orderId = ? and taxiId=? and status=?",
					OrderStatus.CONFIRM_TIMEOUT.getCode(), request.getOrderId(), request.getTaxiId(),
					OrderStatus.PRE_CONFIRM.getCode());
		} else if (status == OrderStatus.CONFIRM) {
			sql = new SimpleSql("update set `order` status=? where orderId = ? and taxiId=? and status=?",
					OrderStatus.CONFIRM.getCode(), request.getOrderId(), request.getTaxiId(),
					OrderStatus.PRE_CONFIRM.getCode());
		} else if (status == OrderStatus.NO_SUPPLY) {
			sql = new SimpleSql("update set `order` status=? where orderId = ? and status=?",
					OrderStatus.NO_SUPPLY.getCode(), request.getOrderId(), OrderStatus.RECORD.getCode());
		}

		if (sql == null) {
			return false;
		}

		return db.update(sql) > 0;
	}

}
