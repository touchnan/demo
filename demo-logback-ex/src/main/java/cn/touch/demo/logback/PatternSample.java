package cn.touch.demo.logback;


import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

public class PatternSample {

	public static void main(String[] args) {
		Logger rootLogger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	    LoggerContext loggerContext = rootLogger.getLoggerContext();
	    // we are not interested in auto-configuration
	    loggerContext.reset();

	    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
	    encoder.setContext(loggerContext);
	    encoder.setPattern("%contextName %-5level [%thread]: %message%n");
	    encoder.start();

	    ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
	    appender.setContext(loggerContext);
	    appender.setEncoder(encoder); 
	    appender.start();

	    rootLogger.addAppender(appender);

	    rootLogger.debug("Message 1"); 
	    rootLogger.warn("Message 2");
	}

}
