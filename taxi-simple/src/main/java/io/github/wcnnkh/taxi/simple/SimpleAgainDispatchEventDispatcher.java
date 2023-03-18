package io.github.wcnnkh.taxi.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.ObjectEvent;
import io.basc.framework.event.support.StandardUnicastDelayableEventDispatcher;
import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.event.AgainDispatchEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleAgainDispatchEventDispatcher extends StandardUnicastDelayableEventDispatcher<ObjectEvent<Order>>
		implements AgainDispatchEventDispatcher {
}
