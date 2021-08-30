package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.DelayableEventDispatcher;
import io.basc.framework.event.ObjectEvent;
import io.github.wcnnkh.taxi.core.dto.Order;

public interface AgainDispatchEventDispatcher extends DelayableEventDispatcher<ObjectEvent<Order>>{
}
