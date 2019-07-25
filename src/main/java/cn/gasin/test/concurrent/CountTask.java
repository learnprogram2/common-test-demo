package cn.gasin.test.concurrent;

import java.util.concurrent.RecursiveTask;

/**
 * @author Wangyk
 * @date 2019/7/25 22:22
 * Fork/Join 框架练习, 把大任务, 分成小任务. 有点像递归
 */
public class CountTask extends RecursiveTask {

	private static final int THRESHOLD = 1;
	private              int start;
	private              int end;

	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int     sum        = 0;
		boolean canCompute = (end - start) <= THRESHOLD;

		//如果任务足够小就计算任务
		if (canCompute) {
			for (int i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			//任务大于阈值, 就分裂成两个字任务计算
			int       middle    = (start + end) / 2;
			CountTask leftTask  = new CountTask(start, middle);
			CountTask rightTask = new CountTask(middle, end);

			leftTask.fork();
			rightTask.fork();

			int leftResult  = (Integer) leftTask.join();
			int rightResult = (Integer) rightTask.join();


			sum = leftResult + rightResult;
		}

		return sum;
	}
}
