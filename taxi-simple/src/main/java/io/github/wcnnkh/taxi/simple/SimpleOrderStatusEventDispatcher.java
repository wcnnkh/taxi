package io.github.wcnnkh.taxi.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.support.SimpleAsyncEventDispatcher;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleOrderStatusEventDispatcher extends SimpleAsyncEventDispatcher<OrderStatusEvent> implements OrderStatusEventDispatcher{
}
