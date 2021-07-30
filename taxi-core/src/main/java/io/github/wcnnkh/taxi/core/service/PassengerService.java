package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.Member;
import io.github.wcnnkh.taxi.core.dto.Passenger;
import io.github.wcnnkh.taxi.core.dto.PostOrderRequest;

public interface PassengerService {
	/**
	 * 乘客位置上报
	 * 
	 * @param message
	 */
	void report(Member message);

	Passenger getPassenger(String passengerId);

	/**
	 * 下单
	 * 
	 * @param request
	 */
	void postOrder(PostOrderRequest request);
}
