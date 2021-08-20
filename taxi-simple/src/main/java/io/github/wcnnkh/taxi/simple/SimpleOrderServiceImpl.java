package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;
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
import scw.util.page.Page;

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
	public boolean updateStatus(UpdateOrderStatusRequest request) {
		Sql sql = null;
		OrderStatus status = request.getStatus();
		if (status == OrderStatus.PRE_CONFIRM) {
			// 预绑定
			sql = new SimpleSql("update `order` set status=?, taxiId = ? where id = ? and status=?",
					OrderStatus.PRE_CONFIRM.getCode(), request.getTaxiId(), request.getOrderId(),
					OrderStatus.RECORD.getCode());
		} else if (status == OrderStatus.CONFIRM_TIMEOUT) {
			sql = new SimpleSql("update `order` set status=? where id = ? and taxiId=? and status=?",
					OrderStatus.CONFIRM_TIMEOUT.getCode(), request.getOrderId(), request.getTaxiId(),
					OrderStatus.PRE_CONFIRM.getCode());
		} else if (status == OrderStatus.CONFIRM) {
			sql = new SimpleSql("update `order` set  status=? where id = ? and taxiId=? and status=?",
					OrderStatus.CONFIRM.getCode(), request.getOrderId(), request.getTaxiId(),
					OrderStatus.PRE_CONFIRM.getCode());
		} else if (status == OrderStatus.NO_SUPPLY) {
			sql = new SimpleSql("update `order` set status=? where id = ? and status=? and taxi is null",
					OrderStatus.NO_SUPPLY.getCode(), request.getOrderId(), OrderStatus.RECORD.getCode());
		} else if (status == OrderStatus.RECEIVE_PASSENGER) {
			sql = new SimpleSql("update `order` set status=? where id = ? and status=?",
					OrderStatus.RECEIVE_PASSENGER.getCode(), request.getOrderId(), OrderStatus.CONFIRM.getCode());
		} else if (status == OrderStatus.ARRIVE) {
			sql = new SimpleSql("update `order` set status=? where id = ? and status=?", OrderStatus.ARRIVE.getCode(),
					request.getOrderId(), OrderStatus.RECEIVE_PASSENGER.getCode());
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

	@Override
	public Page<Order> getPassengerOrders(String passengerId, long pageNumber, long limit) {
		Sql sql = new SimpleSql("select * from `order` where passengerId=? order createTime desc", passengerId);
		return db.getPage(Order.class, sql, pageNumber, limit);
	}

	@Override
	public Page<Order> getTaxiOrders(String taxiId, long pageNumber, long limit) {
		Sql sql = new SimpleSql("select * from `order` where taxiId=? order createTime desc", taxiId);
		return db.getPage(Order.class, sql, pageNumber, limit);
	}

	@Override
	public Order getTaxiOrdersInProgress(String taxiId) {
		Sql sql = new SimpleSql("select * from `order` where taxiId=? order createTime desc limit 0,1", taxiId);
		return db.query(Order.class, sql).first();
	}

	@Override
	public Order getPassengerOrdersInProgress(String passengerId) {
		Sql sql = new SimpleSql("select * from `order` where passengerId=? order createTime desc limit 0,1",
				passengerId);
		return db.query(Order.class, sql).first();
	}
}
