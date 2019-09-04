package cn.gasin.test.concurrent;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author wongyiming
 * @description Semaphore同步器练习
 * @date 2019/9/4 19:35
 */
public class SemaphoreTest {

	private static Semaphore semaphore = new Semaphore(0);

	@Test
	public void test1() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(2);

		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread() + " 1over");
					semaphore.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread() + " 2over");
					Thread.sleep(3000);
					semaphore.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		semaphore.acquire(2);
		System.out.println("1all child Thread Over!");

		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread() + " 3over");
					semaphore.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread() + " 4over");
					Thread.sleep(3000);
					semaphore.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		semaphore.acquire(2);
		System.out.println("2all child Thread Over!");

		executorService.shutdown();
	}


}
