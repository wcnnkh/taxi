package io.github.wcnnkh.taxi.web;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import io.github.wcnnkh.taxi.core.dto.Trace;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.PassengerService;
import scw.core.utils.StringUtils;
import scw.event.EventListener;
import scw.json.JSONUtils;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.web.servlet.socket.ContainerConfigurator;

@ServerEndpoint(value = "/passenger/websocket/{passengerId}", configurator = ContainerConfigurator.class)
public class PassengerWebSocket implements EventListener<OrderStatusEvent> {
	private static Logger logger = LoggerFactory.getLogger(PassengerWebSocket.class);
	private ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();
	private final PassengerService passengerService;

	public PassengerWebSocket(PassengerService passengerService,
			OrderStatusEventDispatcher orderStatusEventDispatcher) {
		this.passengerService = passengerService;
		orderStatusEventDispatcher.registerListener(this);
	}

	@Override
	public void onEvent(OrderStatusEvent event) {
		if (StringUtils.isNotEmpty(event.getOrder().getPassengerId())) {
			Session session = sessionMap.get(event.getOrder().getPassengerId());
			if (session != null) {
				String message = JSONUtils.getJsonSupport().toJSONString(event.getOrder());
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					logger.info(e, message);
				}
			}
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("passengerId") String passengerId) throws IOException {
		logger.info("{}打开连接{}", session.getId(), passengerId);
		Session oldSession = sessionMap.remove(passengerId);
		sessionMap.put(passengerId, session);
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
		Trace trace = JSONUtils.getJsonSupport().parseObject(message, Trace.class);
		if (trace == null) {
			return;
		}
		passengerService.report(trace);
	}
}