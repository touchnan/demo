package cn.touch.demo.logback.kafka;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/12/15.
 */
public class Logback2Kafka {
    /*-
    https://my.oschina.net/infiniteSpace/blog/312063
     */

    public static void main(String[] args) throws IOException, JoranException, InterruptedException {

        load("src/main/resources/logback-kafka.xml");
        Logger logger = LoggerFactory.getLogger(Logback2Kafka.class);
        for (int i = 0; i < 1; i++) {
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

    /*-
        http://blog.csdn.net/bushizhuanjia/article/details/8609437
     */
    public static void load (String externalConfigFileLocation) throws IOException, JoranException {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        File externalConfigFile = new File(externalConfigFileLocation);
        if(!externalConfigFile.exists()){
            throw new IOException("Logback External Config File Parameter does not reference a file that exists");
        }else{
            if(!externalConfigFile.isFile()){
                throw new IOException("Logback External Config File Parameter exists, but does not reference a file");
            }else{
                if(!externalConfigFile.canRead()){
                    throw new IOException("Logback External Config File exists and is a file, but cannot be read.");
                }else{
                    JoranConfigurator configurator = new JoranConfigurator();
                    configurator.setContext(lc);
                    lc.reset();
                    configurator.doConfigure(externalConfigFileLocation);
                    StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
                }
            }
        }
    }
}
