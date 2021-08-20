package io.github.wcnnkh.taxi.core.event;

import io.github.wcnnkh.taxi.core.dto.TaxiOrderRequest;
import scw.event.BasicEvent;

public class GrabOrderEvent extends BasicEvent {
	private static final long serialVersionUID = 1L;
	private final TaxiOrderRequest grabOrderRequest;

	public GrabOrderEvent(TaxiOrderRequest grabOrderRequest) {
		this.grabOrderRequest = grabOrderRequest;
	}

	public TaxiOrderRequest getGrabOrderRequest() {
		return grabOrderRequest;
	}
}
