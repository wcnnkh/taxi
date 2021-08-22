package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.event.DispatchEventDispatcher;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.ObjectEvent;
import scw.event.support.SimpleDelayableEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleDispatchEventDispatcher extends SimpleDelayableEventDispatcher<ObjectEvent<Order>>
		implements DispatchEventDispatcher {
}
