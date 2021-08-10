package io.github.wcnnkh.taxi.simple;

import io.github.wcnnkh.taxi.core.event.ConfirmTimeoutEventDispatcher;
import io.github.wcnnkh.taxi.core.event.GrabOrderEvent;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.event.support.SimpleDelayEventDispatcher;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleConfirmTimeoutEventDispatcher extends SimpleDelayEventDispatcher<GrabOrderEvent>
		implements ConfirmTimeoutEventDispatcher {
}
