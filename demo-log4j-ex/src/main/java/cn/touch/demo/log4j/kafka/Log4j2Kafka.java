package cn.touch.demo.log4j.kafka;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/12/15.
 */
public class Log4j2Kafka {
    public static void main(String[] args) {
//        private static final

        PropertyConfigurator.configure("src/main/resources/log4jKafka.properties");
        Logger logger = LoggerFactory.getLogger(Log4j2Kafka.class);
        for (int i = 0; i < 10; i++) {
            logger.info("输出信息……");
            logger.trace("随意打印……");
            logger.debug("调试信息……");
            logger.warn("警告信息……");
            try {
                throw new Exception("错误消息啊");
            } catch (Exception e) {
                logger.error("处理业务逻辑的时候发生一个错误……", e);
            }
        }

    }
}
