package io.github.wcnnkh.taxi.core.service;

import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;

import java.util.List;

/**
 * 车辆服务
 * 
 * @author shuchaowen
 *
 */
public interface TaxiService {
	/**
	 * 获取附近车牌
	 * 
	 * @param taxiStatus
	 * @return
	 */
	List<Taxi> getNearbyTaxis(TaxiStatus taxiStatus);

	/**
	 * 获取taxi信息
	 * 
	 * @param taxiId
	 * @return
	 */
	Taxi getTaxi(String taxiId);
}
