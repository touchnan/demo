package cn.touch.demo.dubbo;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.touch.demo.dubbo.serv.DemoServiceImpl;

public class TestDubbo {
	private static ApplicationContext papp;
	private static ApplicationContext capp;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 papp = new ClassPathXmlApplicationContext("classpath:dubbo-provider.xml");
		 capp = new ClassPathXmlApplicationContext("classpath:dubbo-consumer.xml");
	}
	
	@Test
	public void testProvider() {
		DemoService serv = papp.getBean(DemoService.class);
		serv.hello("Hcq");
	}
	
	@Test
	public void testConsumer() {
		DemoService serv = capp.getBean(DemoService.class);
		serv.hello("ZZ");
	}

}
