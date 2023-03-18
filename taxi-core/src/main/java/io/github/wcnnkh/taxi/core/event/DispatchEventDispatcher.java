package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.ObjectEvent;
import io.basc.framework.event.UnicastDelayableEventDispatcher;
import io.github.wcnnkh.taxi.core.dto.Order;

/**
 * 调度
 * 
 * @author wcnnkh
 *
 */
public interface DispatchEventDispatcher extends UnicastDelayableEventDispatcher<ObjectEvent<Order>> {

}
