package io.github.wcnnkh.taxi.web;

public enum HeartbeatType {
	/**
	 * 订单信息
	 */
	ORDER,
	/**
	 * 附近车辆
	 */
	NEARBY_TAXI,
	;

	public <T> Heartbeat<T> wrap(T message) {
		Heartbeat<T> heartbeat = new Heartbeat<>();
		heartbeat.setType(name());
		heartbeat.setMessage(message);
		return heartbeat;
	}
}
