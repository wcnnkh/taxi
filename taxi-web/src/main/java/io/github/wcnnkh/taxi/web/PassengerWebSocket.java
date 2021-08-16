package io.github.wcnnkh.taxi.web;

import java.io.IOException;

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
import scw.websocket.adapter.standard.SafeStandardSessionManager;

@ServerEndpoint(value = "/passenger/websocket/{passengerId}", configurator = ContainerConfigurator.class)
public class PassengerWebSocket implements EventListener<OrderStatusEvent> {
	private static Logger logger = LoggerFactory.getLogger(PassengerWebSocket.class);
	private static SafeStandardSessionManager<String> sessionManager = new SafeStandardSessionManager<>("passenger");
	private final PassengerService passengerService;

	public PassengerWebSocket(PassengerService passengerService,
			OrderStatusEventDispatcher orderStatusEventDispatcher) {
		this.passengerService = passengerService;
		orderStatusEventDispatcher.registerListener(this);
	}

	@Override
	public void onEvent(OrderStatusEvent event) {
		if (StringUtils.isNotEmpty(event.getOrder().getPassengerId())) {
			sessionManager.getSessions(event.getOrder().getPassengerId()).stream().forEach((session) -> {
				String message = JSONUtils.getJsonSupport().toJSONString(event.getOrder());
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					logger.info(e, message);
				}
			});
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("passengerId") String passengerId) {
		CloseReason closeReason = new CloseReason(CloseCodes.NORMAL_CLOSURE, "关闭重复的连接");
		sessionManager.remove(passengerId).forEach((s) -> {
			try {
				s.close(closeReason);
			} catch (IOException e) {
				//ignore
			}
		});
		sessionManager.register(passengerId, session);
		sessionManager.putIfAbsent(passengerId, session);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		sessionManager.remove(session);
	}

	@OnError
	public void onError(Session session, Throwable thr) {
		logger.error(thr, "连接错误{}", session.getId());
	}

	@OnMessage
	public void onMessage(String message) {
		//TODO 此处的乘客id应该从连接 中获取 ，taxi同理
		logger.info("位置上报：" + message);
		Trace trace = JSONUtils.getJsonSupport().parseObject(message, Trace.class);
		if (trace == null) {
			return;
		}
		passengerService.report(trace);
	}
}