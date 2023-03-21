package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.BroadcastEventDispatcher;

/**
 * 订单事件分发
 * 
 * @author wcnnkh
 *
 */
public interface OrderStatusEventDispatcher extends BroadcastEventDispatcher<OrderStatusEvent> {
}
