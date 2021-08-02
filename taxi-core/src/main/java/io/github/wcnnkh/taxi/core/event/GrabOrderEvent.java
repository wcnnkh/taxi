package io.github.wcnnkh.taxi.core.event;

import io.github.wcnnkh.taxi.core.dto.GrabOrderRequest;
import scw.event.BasicEvent;

public class GrabOrderEvent extends BasicEvent {
	private static final long serialVersionUID = 1L;
	private final GrabOrderRequest grabOrderRequest;

	public GrabOrderEvent(GrabOrderRequest grabOrderRequest) {
		this.grabOrderRequest = grabOrderRequest;
	}

	public GrabOrderRequest getGrabOrderRequest() {
		return grabOrderRequest;
	}
}
