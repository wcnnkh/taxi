package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.UnicastEventDispatcher;

/**
 * 乘客位置变化
 * 
 * @author wcnnkh
 *
 */
public interface PassengerTraceEventDispatcher extends UnicastEventDispatcher<TraceEvent> {
}
