package io.github.wcnnkh.taxi.simple;

import java.io.File;

import io.github.wcnnkh.taxi.core.dto.Order;
import io.github.wcnnkh.taxi.core.enums.OrderStatus;
import io.github.wcnnkh.taxi.core.event.OrderStatusEvent;
import io.github.wcnnkh.taxi.core.event.OrderStatusEventDispatcher;
import io.github.wcnnkh.taxi.core.service.DispatchPolicyService;
import scw.context.annotation.Provider;
import scw.core.Ordered;
import scw.env.Sys;
import scw.event.EventListener;

/**
 * 简单的调度策略实现
 * 
 * @author 35984
 *
 */
@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SimpleDispatchPolicyService implements DispatchPolicyService, EventListener<OrderStatusEvent> {
	private File lockDirectory = new File(Sys.env.getWorkPath() + "/grab_notify_lock");

	public SimpleDispatchPolicyService(OrderStatusEventDispatcher orderStatusEventDispatcher) {
		orderStatusEventDispatcher.registerListener(this);
	}

	@Override
	public void onEvent(OrderStatusEvent event) {
		if (OrderStatus.PRE_CONFIRM.getCode().equals(event.getOrder().getStatus())) {
			// 如果调度已经完成删除锁文件
			File file = new File(lockDirectory, event.getOrder().getId());
			file.delete();
		}
	}

	@Override
	public boolean canNotifyGrab(String orderId, String taxiId) {
		File file = new File(lockDirectory, "/" + orderId + "/" + taxiId);
		return file.mkdirs();
	}

	@Override
	public boolean isTimeout(Order order) {
		return System.currentTimeMillis() - order.getCreateTime() > order.getDispatchTimeout();
	}
}
