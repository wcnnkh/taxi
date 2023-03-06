package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.dto.PassengerOrderRequest;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;
import io.github.wcnnkh.taxi.core.dto.TaxiOrderRequest;

/**
 * 调度服务
 * 
 * @author wcnnkh
 *
 */
public interface DispatchService {
	Order postOrder(PostOrderRequest request);

	void grabOrder(TaxiOrderRequest request);

	boolean confirmOrder(TaxiOrderRequest request);

	boolean receivePassenger(TaxiOrderRequest request);

	boolean arrive(TaxiOrderRequest request);

	boolean cancel(TaxiOrderRequest request);

	boolean cancel(PassengerOrderRequest request);
}
