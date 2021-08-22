package io.github.wcnnkh.taxi.core.event;

import io.github.wcnnkh.taxi.core.dto.Order;
import scw.event.DelayableEventDispatcher;
import scw.event.ObjectEvent;

public interface DispatchEventDispatcher extends DelayableEventDispatcher<ObjectEvent<Order>> {

}
