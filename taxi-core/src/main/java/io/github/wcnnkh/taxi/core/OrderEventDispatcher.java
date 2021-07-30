package io.github.wcnnkh.taxi.core;

import io.github.wcnnkh.taxi.core.dto.PostOrder;
import scw.event.EventDispatcher;
import scw.event.ObjectEvent;

/**
 * 订单事件分发
 * 
 * @author shuchaowen
 *
 */
public interface OrderEventDispatcher extends EventDispatcher<ObjectEvent<PostOrder>> {
}
