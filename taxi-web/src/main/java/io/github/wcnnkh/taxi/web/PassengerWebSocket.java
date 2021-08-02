package io.github.wcnnkh.taxi.web;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import io.github.wcnnkh.taxi.core.service.PassengerService;
import scw.logger.Logger;
import scw.logger.LoggerFactory;

@ServerEndpoint("/passenger/websocket/{passengerId}")
public class PassengerWebSocket {
	private static Logger logger = LoggerFactory.getLogger(PassengerWebSocket.class);
	private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
	
	private static PassengerService passengerService;

	@OnOpen
	public void onOpen(Session session, @PathParam("passengerId") String passengerId) throws IOException {
		logger.info("{}打开连接{}", session.getId(), passengerId);
		Session oldSession = sessionMap.putIfAbsent(passengerId, session);
		if (oldSession != null) {
			logger.info("关闭重复的连接");
			oldSession.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "关闭重复的连接"));
		}
		session.getUserProperties().put("passengerId", passengerId);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		String passengerId = (String) session.getUserProperties().get("passengerId");
		Session oldSession = sessionMap.remove(passengerId);
		if (oldSession != null) {
			oldSession.close();
		}
	}

	@OnError
	public void onError(Session session, Throwable thr) {
	}

	@OnMessage
	public void onMessage(String message) {
		
	}
}