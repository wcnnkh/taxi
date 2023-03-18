package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.ObjectEvent;
import io.basc.framework.event.UnicastDelayableEventDispatcher;
import io.github.wcnnkh.taxi.core.dto.Order;

/**
 * 重新调度
 * 
 * @author wcnnkh
 *
 */
public interface AgainDispatchEventDispatcher extends UnicastDelayableEventDispatcher<ObjectEvent<Order>> {
}
