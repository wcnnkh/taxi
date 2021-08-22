package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.PassengerOrderRequest;
import io.github.wcnnkh.taxi.core.dto.TaxiOrderRequest;
import io.github.wcnnkh.taxi.core.dto.Order;
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
	Order postOrder(PostOrderRequest request);

	/**
	 * 抢单
	 * 
	 * @param request
	 */
	void grabOrder(TaxiOrderRequest request);
	
	/**
	 * 确认订单
	 * @param request
	 */
	boolean confirmOrder(TaxiOrderRequest request);
	
	/**
	 * 接到乘客
	 * @param request
	 * @return
	 */
	boolean receivePassenger(TaxiOrderRequest request);
	
	/**
	 * 到达目的地
	 * @param request
	 * @return
	 */
	boolean arrive(TaxiOrderRequest request);
	
	/**
	 * 司机取消
	 * @param request
	 * @return
	 */
	boolean cancel(TaxiOrderRequest request);
	
	/**
	 * 乘客取消
	 * @param request
	 * @return
	 */
	boolean cancel(PassengerOrderRequest request);
}
