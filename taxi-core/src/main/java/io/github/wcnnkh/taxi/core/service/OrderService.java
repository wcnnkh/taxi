package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Order;

public interface OrderService {
	Order getOrder(String orderId);
	
	boolean preConfirm(GrabOrderRequest request);
	
	/**
	 * 确认绑定
	 * @param request
	 * @return
	 */
	boolean confirm(GrabOrderRequest request);
}
