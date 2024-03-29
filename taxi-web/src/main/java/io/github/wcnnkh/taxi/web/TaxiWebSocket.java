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

import io.basc.framework.event.EventListener;
import io.basc.framework.json.JsonUtils;
import io.basc.framework.logger.Logger;
import io.basc.framework.logger.LoggerFactory;
import io.basc.framework.util.StringUtils;
import io.basc.framework.websocket.adapter.standard.StandardContainerConfigurator;
import io.basc.framework.websocket.adapter.standard.StandardSessionManager;
import io.github.wcnnkh.taxi.core.dto.Trace;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.TaxiService;

@ServerEndpoint(value = "/taxi/websocket/{taxiId}", configurator = StandardContainerConfigurator.class)
public class TaxiWebSocket implements EventListener<OrderStatusEvent> {
	private static Logger logger = LoggerFactory.getLogger(TaxiWebSocket.class);
	private static StandardSessionManager<String> sessionManager = new StandardSessionManager<String>("taxi");
	private final TaxiService taxiService;

	public TaxiWebSocket(TaxiService taxiService, OrderStatusEventDispatcher orderStatusEventDispatcher) {
		this.taxiService = taxiService;
		orderStatusEventDispatcher.registerListener(this);
	}

	@Override
	public void onEvent(OrderStatusEvent event) {
		if (StringUtils.isNotEmpty(event.getOrder().getTaxiId())) {
			sessionManager.getSessions(event.getOrder().getTaxiId()).stream().filter((session) -> session.isOpen())
					.forEach((session) -> {
						String message = HeartbeatType.ORDER.wrap(event.getOrder()).toString();
						if (logger.isDebugEnabled()) {
							logger.debug("向车辆[{}]推送消息：{}", event.getOrder().getTaxiId(), message);
						}
						try {
							session.getBasicRemote().sendText(message);
						} catch (IOException e) {
							logger.info(e, message);
						}
					});
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("taxiId") String taxiId) throws IOException {
		CloseReason closeReason = new CloseReason(CloseCodes.NORMAL_CLOSURE, "关闭重复的连接");
		sessionManager.remove(taxiId).forEach((s) -> {
			logger.info("关闭重复连接:" + s.getId());
			try {
				s.close(closeReason);
			} catch (IOException e) {
				// ignore
			}
		});
		sessionManager.register(taxiId, session);
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
		if(logger.isTraceEnabled()) {
			logger.trace("On message: {}", message);
		}
		
		Trace trace = JsonUtils.getSupport().parseObject(message, Trace.class);
		if (trace == null) {
			return;
		}
		taxiService.report(trace);
	}
}
