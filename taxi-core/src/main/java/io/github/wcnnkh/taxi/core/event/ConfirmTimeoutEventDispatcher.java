package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.UnicastDelayableEventDispatcher;

/**
 * 确认超时事件
 * 
 * @author shuchaowen
 *
 */
public interface ConfirmTimeoutEventDispatcher extends UnicastDelayableEventDispatcher<GrabOrderEvent> {

}
