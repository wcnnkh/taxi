package io.github.wcnnkh.taxi.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.support.SimpleAsyncEventDispatcher;
import io.github.wcnnkh.taxi.core.event.TaxiTraceEventDispatcher;
import io.github.wcnnkh.taxi.core.event.TraceEvent;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleTaxiTraceEventDispatcher extends SimpleAsyncEventDispatcher<TraceEvent>
		implements TaxiTraceEventDispatcher {
}
