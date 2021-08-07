package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;

/**
 * 调度服务
 * @author shuchaowen
 *
 */
public interface DispatchService {
	/**
	 * 下单
	 * 
	 * @param request
	 */
	void postOrder(PostOrderRequest request);

	/**
	 * 抢单
	 * 
	 * @param request
	 */
	void grabOrder(GrabOrderRequest request);
}