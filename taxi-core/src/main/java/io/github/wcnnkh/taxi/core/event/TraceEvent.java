package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.ObjectEvent;
import io.github.wcnnkh.taxi.core.dto.Trace;

public class TraceEvent extends ObjectEvent<Trace> {
	private static final long serialVersionUID = 1L;

	public TraceEvent(Trace source) {
		super(source);
	}
}
