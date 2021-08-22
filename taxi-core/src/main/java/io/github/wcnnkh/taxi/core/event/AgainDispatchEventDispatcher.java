package io.github.wcnnkh.taxi.core.event;

import io.github.wcnnkh.taxi.core.dto.Order;
import scw.event.DelayableEventDispatcher;
import scw.event.ObjectEvent;

public interface AgainDispatchEventDispatcher extends DelayableEventDispatcher<ObjectEvent<Order>>{
}
