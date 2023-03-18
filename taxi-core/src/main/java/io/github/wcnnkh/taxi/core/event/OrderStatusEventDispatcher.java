package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.UnicastEventDispatcher;

/**
 * 订单事件分发
 * 
 * @author wcnnkh
 *
 */
public interface OrderStatusEventDispatcher extends UnicastEventDispatcher<OrderStatusEvent> {
}
