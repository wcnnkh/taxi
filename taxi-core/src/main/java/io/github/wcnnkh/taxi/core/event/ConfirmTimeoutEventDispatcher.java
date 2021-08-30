package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.DelayableEventDispatcher;

/**
 * 确认超时事件
 * @author shuchaowen
 *
 */
public interface ConfirmTimeoutEventDispatcher extends DelayableEventDispatcher<GrabOrderEvent>{
	
}
