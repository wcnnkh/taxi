package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.Order;

/**
 * 调度策略服务
 * 
 * @author shuchaowen
 *
 */
public interface DispatchPolicyService {
	/**
	 * 是否可以通知抢单
	 * 
	 * @param orderId
	 * @param taxiId
	 * @return
	 */
	boolean canNotifyGrab(String orderId, String taxiId);
	
	/**
	 * 调度是否超时
	 * @param order
	 * @return
	 */
	boolean isTimeout(Order order);
}
