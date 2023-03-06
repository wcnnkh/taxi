package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.Order;

/**
 * 调度策略服务
 * 
 * @author shuchaowen
 *
 */
public interface DispatchPolicyService {
	boolean canNotifyGrab(String orderId, String taxiId);

	boolean isTimeout(Order order);
}
