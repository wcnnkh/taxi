package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.support.DefaultAsyncEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleOrderStatusEventDispatcher extends DefaultAsyncEventDispatcher<OrderStatusEvent> implements OrderStatusEventDispatcher{
}
