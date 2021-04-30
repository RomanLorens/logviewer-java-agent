package rl.lv.agent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompletionExecutor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private int poolSize = 4;
	private int queueSize = 1000;
	private long timeout = 15;
	private final ExecutorCompletionService<Object> completionService;
	private final ThreadPoolExecutor executor;

	public CompletionExecutor() {
		ThreadFactory tf = (r) -> {
			Thread t = new Thread(r);
			t.setName("Completion-Executor-Thread-" + t.getId());
			return t;
		};
		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(queueSize);
		executor = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.SECONDS, queue, tf,
				new ThreadPoolExecutor.CallerRunsPolicy());
		completionService = new ExecutorCompletionService<Object>(executor);
		logger.info("Initialized exeutor completion service with {} threads", poolSize);
	}

	public void submit(Callable<Object> task) {
		completionService.submit(task);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> waitForTasks(int n, Class<T> response) {
		List<T> res = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			try {
				Future<Object> future = (Future<Object>) completionService.poll(timeout, TimeUnit.SECONDS);
				Object o = future.get();
				if (o instanceof List) {
					res.addAll((List<T>) future.get());
				} else if (o != null){
					res.add((T) o);
				}
			} catch (Exception e) {
				logger.error("Task failed {}", e.getMessage());
			}
		}
		return res;
	}

	public void shutdown() {
		logger.info("shutting down executor");
		executor.shutdown();
	}

}
