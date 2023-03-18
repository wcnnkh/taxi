package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.UnicastEventDispatcher;

/**
 * 车辆位置变化
 * 
 * @author wcnnkh
 *
 */
public interface TaxiTraceEventDispatcher extends UnicastEventDispatcher<TraceEvent> {
}
