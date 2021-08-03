package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.event.PassengerTraceEventDispatcher;
import io.github.wcnnkh.taxi.core.event.TraceEvent;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.support.DefaultAsyncEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimplePassengerTraceEventDispatcher extends DefaultAsyncEventDispatcher<TraceEvent> implements PassengerTraceEventDispatcher{
}
