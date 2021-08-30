package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.DelayableEventDispatcher;
import io.basc.framework.event.ObjectEvent;
import io.github.wcnnkh.taxi.core.dto.Order;

public interface DispatchEventDispatcher extends DelayableEventDispatcher<ObjectEvent<Order>> {

}
