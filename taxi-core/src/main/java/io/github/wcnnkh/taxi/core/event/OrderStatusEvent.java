package io.github.wcnnkh.taxi.core.event;

import io.basc.framework.event.BasicEvent;
import io.github.wcnnkh.taxi.core.dto.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrderStatusEvent extends BasicEvent {
	private static final long serialVersionUID = 1L;
	private final Order oldOrder;
	private final Order order;
}
