package io.github.wcnnkh.taxi.simple;

import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.ObjectEvent;
import scw.event.support.SimpleDelayableEventDispatcher;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.event.AgainDispatchEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleAgainDispatchEventDispatcher extends
		SimpleDelayableEventDispatcher<ObjectEvent<Order>> implements
		AgainDispatchEventDispatcher {

}
