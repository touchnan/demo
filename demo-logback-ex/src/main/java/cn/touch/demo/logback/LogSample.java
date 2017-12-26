package cn.touch.demo.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogSample {
	public static void main(String[] args) {
		Logger logger = (Logger)LoggerFactory.getLogger(LogSample.class);
		
		logger.debug("真的是测试");
		logger.error("真的是错误 ");
	}
}
