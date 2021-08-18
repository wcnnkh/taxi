package io.github.wcnnkh.taxi.web;

public enum HeartbeatType {
	ORDER,
	;

	public <T> Heartbeat<T> wrap(T message) {
		Heartbeat<T> heartbeat = new Heartbeat<>();
		heartbeat.setType(name());
		heartbeat.setMessage(message);
		return heartbeat;
	}
}
