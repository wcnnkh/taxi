package io.github.wcnnkh.taxi.core;

import io.github.wcnnkh.taxi.core.dto.PostOrder;
import scw.event.EventDispatcher;
import scw.event.ObjectEvent;

public interface PassengerLocationEventDispatcher extends EventDispatcher<ObjectEvent<PostOrder>> {
}
