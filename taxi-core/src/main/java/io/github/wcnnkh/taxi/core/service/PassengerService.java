package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.Passenger;
import io.github.wcnnkh.taxi.core.dto.Trace;

public interface PassengerService {
	void report(Trace trace);

	Passenger getPassenger(String passengerId);
}
