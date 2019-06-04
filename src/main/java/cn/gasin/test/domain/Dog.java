package cn.gasin.test.domain;

import cn.gasin.cp.anno.MyAnnotation;
import org.junit.Test;

/**
 * @author wongyiming
 * @date 2019/6/4 16:54
 */
@MyAnnotation("aaaaaaaaa")
public class Dog {

	@MyAnnotation("aaaaaaaaa")
	public void walk() {
		System.out.println("小狗跳啊跳");
	}


	@Test
	public void test() throws Exception {
		System.out.println("sss");

	}

}
