/**
 * 
 */
package cn.touch.lab.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 2, 2017.
 * 
 */
public class TestSpring {
	private ApplicationContext context;
	
	@Before
	public void before() {
		context =
			    new ClassPathXmlApplicationContext(new String[] {"spring/context.xml"});
	}
	
	@Test
	public void t() {
//	    IUserService serv = context.getBean(IUserService.class);
//		System.out.println(serv.validate());
		System.out.println(context);
	}

}
