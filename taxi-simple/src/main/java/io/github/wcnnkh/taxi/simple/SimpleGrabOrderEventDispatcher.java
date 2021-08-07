package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.event.GrabOrderEventDispatcher;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.support.DefaultAsyncEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleGrabOrderEventDispatcher extends DefaultAsyncEventDispatcher<GrabOrderEvent>
		implements GrabOrderEventDispatcher {
}