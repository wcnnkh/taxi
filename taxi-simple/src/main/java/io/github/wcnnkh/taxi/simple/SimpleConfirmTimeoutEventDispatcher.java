package io.github.wcnnkh.taxi.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.event.support.SimpleDelayableEventDispatcher;
import io.github.wcnnkh.taxi.core.event.ConfirmTimeoutEventDispatcher;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleConfirmTimeoutEventDispatcher extends SimpleDelayableEventDispatcher<GrabOrderEvent>
		implements ConfirmTimeoutEventDispatcher {
}
