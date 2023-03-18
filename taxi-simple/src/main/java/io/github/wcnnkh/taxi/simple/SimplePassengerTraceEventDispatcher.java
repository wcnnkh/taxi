package io.github.wcnnkh.taxi.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.support.StandardUnicastEventDispatcher;
import io.basc.framework.util.Selector;
import io.basc.framework.util.concurrent.TaskQueue;
import io.github.wcnnkh.taxi.core.event.PassengerTraceEventDispatcher;
import io.github.wcnnkh.taxi.core.event.TraceEvent;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimplePassengerTraceEventDispatcher extends StandardUnicastEventDispatcher<TraceEvent>
		implements PassengerTraceEventDispatcher {

	public SimplePassengerTraceEventDispatcher() {
		super(Selector.roundRobin(), new TaskQueue());
	}
}
