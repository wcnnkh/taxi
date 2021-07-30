package io.github.wcnnkh.taxi.core;

import io.github.wcnnkh.taxi.core.dto.Member;
import scw.event.ChangeEvent;
import scw.event.EventDispatcher;

/**
 * 位置变化事件注册
 * 
 * @author shuchaowen
 *
 */
public interface TaxiLocationEventDispatcher extends EventDispatcher<ChangeEvent<Member>> {
}
