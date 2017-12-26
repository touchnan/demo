package cn.touch.demo.log4j.jms.activemq;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/12/15.
 */

/**-
 * http://activemq.apache.org/how-do-i-use-log4j-jms-appender-with-activemq.html
 * https://my.oschina.net/itblog/blog/533730?fromerr=4B3HCXff
 *
 * http://activemq.apache.org/javaxjmsjmsexception-wire-format-negociation-timeout-peer-did-not-send-his-wire-format.html
 */
public class Log4Jms {
    public static void main(String[] args) {
    	PropertyConfigurator.configure("src/main/resources/log4jActiveMQ.properties");
    	Logger log = LoggerFactory.getLogger(Log4Jms.class);
        log.info("Info Log.");
        log.warn("Warn Log");
        log.error("Error Log.");
        System.exit(1);
    }
}
