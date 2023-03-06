package io.github.wcnnkh.taxi.core.service;

import java.util.List;

import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;
import io.github.wcnnkh.taxi.core.dto.Trace;

/**
 * 车辆服务
 * 
 * @author shuchaowen
 *
 */
public interface TaxiService {
	void report(Trace trace);

	List<Taxi> getNearbyTaxis(NearbyTaxiQuery query);

	Taxi getTaxi(String taxiId);

	void setStatus(String taxiId, TaxiStatus taxiStatus);
}
