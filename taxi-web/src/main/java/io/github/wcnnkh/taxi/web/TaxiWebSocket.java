package io.github.wcnnkh.taxi.web;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/taxi/websocket")
public class TaxiWebSocket extends Endpoint {
	
	
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		
	}

	@Override
	public void onClose(Session session, CloseReason closeReason) {
	}

	@Override
	public void onError(Session session, Throwable thr) {
	}

	@OnMessage
	public void onMessage(String message) {

	}
}
