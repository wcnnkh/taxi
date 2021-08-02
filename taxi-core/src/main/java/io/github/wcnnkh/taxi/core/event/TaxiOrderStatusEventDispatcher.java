package io.github.wcnnkh.taxi.core.event;

import scw.event.NamedEventDispatcher;

/**
 * 司机相关的订单状态事件分发
 * @author shuchaowen
 *
 */
public interface TaxiOrderStatusEventDispatcher extends NamedEventDispatcher<String, OrderStatusEvent> {
}
