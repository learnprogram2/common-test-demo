package cn.gasin.test.concurrent;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wongyiming
 * @description CyclicBarrier 线程同步屏障的练习
 * @date 2019/9/4 16:43
 */
public class CyclicBarrierTest {

	public static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
	public static CyclicBarrier cyclicBarrierWithRunnable = new CyclicBarrier(2
			, () -> System.out.println(Thread.currentThread() + " task1 merge result"));

	@Test
	public void test1() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(2);

		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread() + "task1-1");
					cyclicBarrierWithRunnable.await();
					System.out.println(Thread.currentThread() + "task1-1 out");
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		});
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread() + "task1-2");
					cyclicBarrierWithRunnable.await();
					System.out.println(Thread.currentThread() + "task1-2 out");
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		});

		executorService.shutdown();
	}

}
