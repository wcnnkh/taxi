package io.github.wcnnkh.taxi.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Members;
import io.basc.framework.core.Ordered;
import io.basc.framework.db.DB;
import io.basc.framework.mapper.Copy;
import io.basc.framework.sql.SimpleSql;
import io.basc.framework.sql.Sql;
import io.basc.framework.sql.orm.TableStructure;
import io.basc.framework.util.XUtils;
import io.basc.framework.util.page.Pagination;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.service.OrderService;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleOrderServiceImpl implements OrderService {
	private final DB db;

	public SimpleOrderServiceImpl(DB db) {
		this.db = db;
		TableStructure tableStructure = db.getMapper().getStructure(Order.class).withMethod(Members.DIRECT)
				.withEntitysAfter((e) -> e.setNameNestingDepth(1)).all().filter((e) -> !e.isEntity());
		db.createTable(tableStructure);
		db.getMapper().registerStructure(Order.class, tableStructure);
	}

	@Override
	public Order getOrder(String orderId) {
		return db.getById(Order.class, orderId);
	}

	@Override
	public boolean updateStatus(UpdateOrderStatusRequest request) {
		Sql sql = null;
		OrderStatus status = request.getStatus();
		if (status == OrderStatus.RECORD) {
			sql = new SimpleSql("update `order` set status=?, taxiId = null where id = ? and taxiId=? and status=?",
					OrderStatus.RECORD.getCode(), request.getOrderId(), request.getTaxiId(),
					OrderStatus.CONFIRM_TIMEOUT.getCode());
		} else if (status == OrderStatus.PRE_CONFIRM) {
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
			sql = new SimpleSql("update `order` set status=? where id = ? and status=? and taxiId is null",
					OrderStatus.NO_SUPPLY.getCode(), request.getOrderId(), OrderStatus.RECORD.getCode());
		} else if (status == OrderStatus.RECEIVE_PASSENGER) {
			sql = new SimpleSql("update `order` set status=? where id = ? and status=?",
					OrderStatus.RECEIVE_PASSENGER.getCode(), request.getOrderId(), OrderStatus.CONFIRM.getCode());
		} else if (status == OrderStatus.ARRIVE) {
			sql = new SimpleSql("update `order` set status=? where id = ? and status=?", OrderStatus.ARRIVE.getCode(),
					request.getOrderId(), OrderStatus.RECEIVE_PASSENGER.getCode());
		} else if (status == OrderStatus.PASSENGER_CANCEL) {
			// 在接到乘客前都可以取消
			sql = new SimpleSql("update `order` set status=? where id = ? and passengerId=? status in(?,?,?,?,?)",
					OrderStatus.PASSENGER_CANCEL.getCode(), request.getOrderId(), request.getPassengerId(),
					OrderStatus.RECORD.getCode(), OrderStatus.PRE_CONFIRM.getCode(),
					OrderStatus.CONFIRM_TIMEOUT.getCode(), OrderStatus.CONFIRM.getCode(),
					OrderStatus.RECEIVE_PASSENGER.getCode());
		} else if (status == OrderStatus.TAXI_CANCEL) {
			// 在接到乘客前都可以取消
			sql = new SimpleSql("update `order` set status=? where id = ? and taxiId=? status in(?,?)",
					OrderStatus.TAXI_CANCEL.getCode(), request.getOrderId(), request.getTaxiId(),
					OrderStatus.CONFIRM.getCode(), OrderStatus.RECEIVE_PASSENGER.getCode());
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
		Copy.copy(request, order);
		order.setStatus(OrderStatus.RECORD.getCode());
		order.setCreateTime(System.currentTimeMillis());
		db.save(order);
		return order;
	}

	@Override
	public Pagination<Order> getPassengerOrders(String passengerId, long pageNumber, long limit) {
		Sql sql = new SimpleSql("select * from `order` where passengerId=? order by createTime desc", passengerId);
		return db.getPage(Order.class, sql, pageNumber, limit);
	}

	@Override
	public Pagination<Order> getTaxiOrders(String taxiId, long pageNumber, long limit) {
		Sql sql = new SimpleSql("select * from `order` where taxiId=? order by createTime desc", taxiId);
		return db.getPage(Order.class, sql, pageNumber, limit);
	}

	@Override
	public Order getTaxiOrdersInProgress(String taxiId) {
		Sql sql = new SimpleSql(
				"select * from `order` where taxiId=? and status in (?,?) order by createTime desc limit 0,1", taxiId,
				OrderStatus.CONFIRM, OrderStatus.RECEIVE_PASSENGER.getCode());
		return db.query(Order.class, sql).first();
	}

	@Override
	public Order getPassengerOrdersInProgress(String passengerId) {
		Sql sql = new SimpleSql(
				"select * from `order` where passengerId=? and status in(?,?,?,?,?) order by createTime desc limit 0,1",
				passengerId, OrderStatus.RECORD.getCode(), OrderStatus.PRE_CONFIRM.getCode(),
				OrderStatus.CONFIRM.getCode(), OrderStatus.CONFIRM_TIMEOUT.getCode(),
				OrderStatus.RECEIVE_PASSENGER.getCode());
		return db.query(Order.class, sql).first();
	}
}
