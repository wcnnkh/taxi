package io.github.wcnnkh.taxi.core.service;

import io.basc.framework.util.page.Pagination;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.dto.UpdateOrderStatusRequest;

public interface OrderService {
	Order getOrder(String orderId);

	Order record(PostOrderRequest request);

	boolean updateStatus(UpdateOrderStatusRequest request);

	Pagination<Order> getPassengerOrders(String passengerId, long pageNumber, long limit);

	Pagination<Order> getTaxiOrders(String taxiId, long pageNumber, long limit);

	Order getTaxiOrdersInProgress(String taxiId);

	Order getPassengerOrdersInProgress(String passengerId);
}
