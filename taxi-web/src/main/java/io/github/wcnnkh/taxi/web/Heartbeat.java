package io.github.wcnnkh.taxi.web;

import java.io.Serializable;

import io.basc.framework.json.JsonUtils;

/**
 * 心跳
 * 
 * @author shuchaowen
 *
 * @param <T>
 */
public class Heartbeat<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String type;
	private T message;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return JsonUtils.getSupport().toJsonString(this);
	}
}
