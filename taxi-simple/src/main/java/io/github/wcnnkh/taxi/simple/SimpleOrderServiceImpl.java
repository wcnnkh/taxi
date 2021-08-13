package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.service.OrderService;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.db.DB;
import scw.mapper.Copy;
import scw.orm.sql.StandardTableStructure;
import scw.orm.sql.TableStructure;
import scw.orm.sql.TableStructureMapProcessor;
import scw.sql.SimpleSql;
import scw.sql.Sql;
import scw.util.XUtils;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleOrderServiceImpl implements OrderService {
	private final DB db;
	private final TableStructure tableStructure = StandardTableStructure.wrapper(Order.class);

	public SimpleOrderServiceImpl(DB db) {
		this.db = db;
		db.createTable(tableStructure);
		db.getMapper().register(Order.class, new TableStructureMapProcessor<>(tableStructure));
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

	@Override
	public Order record(PostOrderRequest request) {
		Order order = new Order();
		order.setId(XUtils.getUUID());
		Copy.copy(order, request);
		order.setStatus(OrderStatus.RECORD.getCode());
		order.setCreateTime(System.currentTimeMillis());
		db.save(tableStructure, order);
		return order;
	}
}
