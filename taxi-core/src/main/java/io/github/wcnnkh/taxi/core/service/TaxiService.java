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
	/**
	 * 车辆位置上报
	 * @param trace
	 */
	void report(Trace trace);

	/**
	 * 获取附近车牌
	 * 
	 * @param taxiStatus
	 * @return
	 */
	List<Taxi> getNearbyTaxis(NearbyTaxiQuery query);
 
	/**
	 * 获取taxi信息
	 * 
	 * @param taxiId
	 * @return
	 */
	Taxi getTaxi(String taxiId);
	
	void setStatus(String taxiId, TaxiStatus taxiStatus);
}
