package io.github.wcnnkh.taxi.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.support.StandardBroadcastEventDispatcher;
import io.basc.framework.util.concurrent.TaskQueue;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleOrderStatusEventDispatcher extends StandardBroadcastEventDispatcher<OrderStatusEvent>
		implements OrderStatusEventDispatcher {
	public SimpleOrderStatusEventDispatcher() {
		super(new TaskQueue());
	}
}