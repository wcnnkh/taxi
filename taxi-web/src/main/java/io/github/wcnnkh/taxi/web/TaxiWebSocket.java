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
import io.github.wcnnkh.taxi.core.service.TaxiService;
import scw.core.utils.StringUtils;
import scw.event.EventListener;
import scw.json.JSONUtils;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.web.servlet.socket.ContainerConfigurator;

@ServerEndpoint(value = "/taxi/websocket/{taxiId}", configurator = ContainerConfigurator.class)
public class TaxiWebSocket implements EventListener<OrderStatusEvent> {
	private static Logger logger = LoggerFactory.getLogger(TaxiWebSocket.class);
	private ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();
	private final TaxiService taxiService;

	public TaxiWebSocket(TaxiService taxiService, OrderStatusEventDispatcher orderStatusEventDispatcher) {
		this.taxiService = taxiService;
		orderStatusEventDispatcher.registerListener(this);
	}

	@Override
	public void onEvent(OrderStatusEvent event) {
		if (StringUtils.isNotEmpty(event.getOrder().getTaxiId())) {
			Session session = sessionMap.get(event.getOrder().getTaxiId());
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
	public void onOpen(Session session, @PathParam("taxiId") String taxiId) throws IOException {
		logger.info("{}打开连接{}", session.getId(), taxiId);
		Session oldSession = sessionMap.remove(taxiId);
		sessionMap.put(taxiId, session);
		if (oldSession != null) {
			logger.info("关闭重复的连接:{}", oldSession.getId());
			oldSession.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "关闭重复的连接"));
		}
		session.getUserProperties().put("taxiId", taxiId);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		String taxiId = (String) session.getUserProperties().get("taxiId");
		logger.info("{}关闭连接{}", session.getId(), taxiId);
		Session oldSession = sessionMap.remove(taxiId);
		if (oldSession != null) {
			oldSession.close();
		}
	}

	@OnError
	public void onError(Session session, Throwable thr) {
		logger.error(thr, "连接错误{}", session.getId());
	}

	@OnMessage
	public void onMessage(String message) {
		Trace trace = JSONUtils.getJsonSupport().parseObject(message, Trace.class);
		if (trace == null) {
			return;
		}
		taxiService.report(trace);
	}
}
