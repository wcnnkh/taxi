package io.github.wcnnnkh;

import scw.util.concurrent.TaskQueue;

public class QueueTest {
	public static void main(String[] args) {
		TaskQueue taskQueue = new TaskQueue();
		taskQueue.setDaemon(false);
		taskQueue.start();
		taskQueue.submit(() -> {
			throw new RuntimeException();
		});
	}
}
