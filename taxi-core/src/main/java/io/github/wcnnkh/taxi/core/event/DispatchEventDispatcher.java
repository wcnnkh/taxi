package io.github.wcnnkh.taxi.core.event;

import io.github.wcnnkh.taxi.core.dto.Order;
import scw.event.DelayEventDispatcher;
import scw.event.ObjectEvent;

public interface DispatchEventDispatcher extends DelayEventDispatcher<ObjectEvent<Order>> {

}
