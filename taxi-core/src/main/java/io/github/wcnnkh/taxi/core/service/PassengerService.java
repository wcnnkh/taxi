package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.Passenger;
import io.github.wcnnkh.taxi.core.dto.Trace;

public interface PassengerService {
	/**
	 * 乘客位置上报
	 * 
	 * @param trace
	 */
	void report(Trace trace);

	Passenger getPassenger(String passengerId);
}
