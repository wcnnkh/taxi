package io.github.wcnnkh.taxi.web;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

public class WebsocketSessionManager<T> {
	private final String groupKey;
	private ConcurrentHashMap<T, ConcurrentHashMap<String, Session>> groupMap = new ConcurrentHashMap<>();

	public WebsocketSessionManager(String groupKey) {
		this.groupKey = groupKey;
	}

	public final String getGroupKey() {
		return groupKey;
	}

	@SuppressWarnings("unchecked")
	public T getGroup(Session session) {
		return (T) session.getUserProperties().get(groupKey);
	}

	public void setGroup(Session session, T group) {
		session.getUserProperties().put(groupKey, group);
	}

	public boolean register(Session session) {
		T group = getGroup(session);
		if (group == null) {
			return false;
		}

		while (true) {
			if (groupMap.contains(groupKey)) {
				if (groupMap
						.computeIfAbsent(
								group,
								(groupId) -> {
									ConcurrentHashMap<String, Session> sessionMap = groupMap
											.get(groupKey);
									if (sessionMap == null) {
										sessionMap = new ConcurrentHashMap<String, Session>();
									}

									sessionMap.put(session.getId(), session);
									return sessionMap;
								}) == null) {
					// cas重试
					continue;
				}
				return true;
			} else {
				ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>(
						4);
				if (sessionMap.putIfAbsent(session.getId(), session) != null) {
					// 理论上不是可耻的到这 因为session.id相当于uuid
					return false;
				}

				if (groupMap.putIfAbsent(group, sessionMap) != null) {
					// CAS 重试
					continue;
				}
				return true;
			}
		}
	}

	public boolean remove(Session session) {
		T group = getGroup(session);
		if (group == null) {
			return false;
		}

		ConcurrentHashMap<String, Session> cacheMap = groupMap.get(groupKey);
		if (cacheMap == null) {
			return false;
		}

		if (cacheMap.remove(session.getId()) == null) {
			return false;
		}

		return true;
	}

	public Collection<Session> getSessions(T group) {
		ConcurrentHashMap<String, Session> sessionMap = groupMap.get(group);
		if (sessionMap == null) {
			return Collections.emptyList();
		}

		return sessionMap.values();
	}

	public Collection<T> getGroups() {
		return Collections.list(groupMap.keys());
	}

	public Collection<Session> clear(T group){
		Map<String, Session> sessionMap = groupMap.remove(group);
		if(sessionMap == null){
			return Collections.emptyList();
		}
		
		return sessionMap.values();
	}
}
