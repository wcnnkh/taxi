package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.UnicastEventDispatcher;

/**
 * 抢单
 * 
 * @author wcnnkh
 *
 */
public interface GrabOrderEventDispatcher extends UnicastEventDispatcher<GrabOrderEvent> {
}
