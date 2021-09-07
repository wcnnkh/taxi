package io.github.wcnnkh.taxi.web;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
import io.basc.framework.json.JSONUtils;
import io.basc.framework.logger.Logger;
import io.basc.framework.logger.LoggerFactory;
import io.basc.framework.util.StringUtils;
import io.basc.framework.websocket.adapter.standard.ContainerConfigurator;
import io.basc.framework.websocket.adapter.standard.SafeStandardSessionManager;
import io.github.wcnnkh.taxi.core.dto.NearbyTaxiQuery;
import io.github.wcnnkh.taxi.core.dto.Taxi;
import io.github.wcnnkh.taxi.core.dto.TaxiStatus;
import io.github.wcnnkh.taxi.core.dto.Trace;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.PassengerService;
import io.github.wcnnkh.taxi.core.service.TaxiService;

@ServerEndpoint(value = "/passenger/websocket/{passengerId}", configurator = ContainerConfigurator.class)
public class PassengerWebSocket implements EventListener<OrderStatusEvent> {
	private static Logger logger = LoggerFactory.getLogger(PassengerWebSocket.class);
	private static SafeStandardSessionManager<String> sessionManager = new SafeStandardSessionManager<>("passenger");
	private final PassengerService passengerService;
	private final TaxiService taxiService;
	private final Executor pushExecutor = Executors.newWorkStealingPool();

	public PassengerWebSocket(PassengerService passengerService, OrderStatusEventDispatcher orderStatusEventDispatcher,
			TaxiService taxiService) {
		this.passengerService = passengerService;
		orderStatusEventDispatcher.registerListener(this);
		this.taxiService = taxiService;
	}

	@Override
	public void onEvent(OrderStatusEvent event) {
		if (StringUtils.isNotEmpty(event.getOrder().getPassengerId())) {
			sessionManager.getSessions(event.getOrder().getPassengerId()).stream().forEach((session) -> {
				String message = HeartbeatType.ORDER.wrap(event.getOrder()).toString();
				if (logger.isTraceEnabled()) {
					logger.trace("向乘客[{}]推送消息：{}", event.getOrder().getPassengerId(), message);
				}
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					logger.error(e, message);
				}
			});
		}
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("passengerId") String passengerId) {
		CloseReason closeReason = new CloseReason(CloseCodes.NORMAL_CLOSURE, "关闭重复的连接");
		sessionManager.remove(passengerId).forEach((s) -> {
			logger.info("关闭重复连接:" + s.getId());
			try {
				s.close(closeReason);
			} catch (IOException e) {
				// ignore
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
	public void onMessage(Session session, String message) {
		// TODO 此处的乘客id应该从连接中获取 ，taxi同理
		if (logger.isTraceEnabled()) {
			logger.trace("On message: {}", message);
		}

		Trace trace = JSONUtils.getJsonSupport().parseObject(message, Trace.class);
		if (trace == null) {
			return;
		}
		
		passengerService.report(trace);
		pushExecutor.execute(() -> {
			if(!session.isOpen()) {
				return ;
			}
			NearbyTaxiQuery nearbyTaxiQuery = new NearbyTaxiQuery();
			nearbyTaxiQuery.setLocation(trace.getLocation());
			TaxiStatus taxiStatus = new TaxiStatus();
			taxiStatus.setWorking(true);
			nearbyTaxiQuery.setTaxiStatus(taxiStatus);
			List<Taxi> taxis = taxiService.getNearbyTaxis(nearbyTaxiQuery);
			String pushMessage = HeartbeatType.NEARBY_TAXI.wrap(taxis).toString();
			if(!session.isOpen()) {
				return ;
			}
			synchronized (session) {
				try {
					session.getBasicRemote().sendText(pushMessage);
				} catch (IOException e) {
					logger.error(e, "push message error: {}", pushMessage);
				}
			}
		});
	}
}