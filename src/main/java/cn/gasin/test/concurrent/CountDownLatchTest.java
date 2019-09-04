package cn.gasin.test.concurrent;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author wongyiming
 * @description countDownLatch练习
 * @date 2019/9/3 22:33
 */
public class CountDownLatchTest {


	@Test
	public void test1() throws Exception {
		CountDownLatch countDownLatch = new CountDownLatch(2);

		ExecutorService executorService = Executors.newFixedThreadPool(2);


		executorService.submit(() -> {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
			System.out.println("1");
		});
		executorService.submit(() -> {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
			}
			System.out.println("2");
		});
		System.out.println("wait all child thread over");
		countDownLatch.await();
		System.out.println("finish");
		executorService.shutdown();

	}
}
