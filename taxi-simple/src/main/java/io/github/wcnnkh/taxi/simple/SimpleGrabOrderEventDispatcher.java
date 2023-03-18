package io.github.wcnnkh.taxi.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.support.StandardUnicastEventDispatcher;
import io.basc.framework.util.Selector;
import io.basc.framework.util.concurrent.TaskQueue;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import io.github.wcnnkh.taxi.core.event.GrabOrderEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleGrabOrderEventDispatcher extends StandardUnicastEventDispatcher<GrabOrderEvent>
		implements GrabOrderEventDispatcher {

	public SimpleGrabOrderEventDispatcher() {
		super(Selector.roundRobin(), new TaskQueue());
	}
}
