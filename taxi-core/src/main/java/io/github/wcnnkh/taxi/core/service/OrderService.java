package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.Order;
import scw.context.result.Result;

public interface OrderService {
	Order getOrder(String orderId);

	/**
	 * 绑定车牌和订单的关系
	 * 
	 * @param taxiId
	 * @param orderId
	 * @return
	 */
	Result bindOrder(String taxiId, String orderId);
}
