package cn.touch.demo.log4j.jms.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggingEvent;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class Log4jJmsCunsumer {

    public static void main(String[] args) throws JMSException, InterruptedException {
        // TODO Auto-generated method stub
        PropertyConfigurator.configure("src/main/resources/log4j-empty.properties");

        ActiveMQConnectionFactory factory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = factory.createConnection();

        /*-
        http://blog.csdn.net/sunyx1130/article/details/51942656
        ActiveMQ的ObjectMessage依赖于Java的序列化和反序列化，但是这个过程被认为是不安全的。所以ActiveMQ就强制用户使用完整路径的ObjectMessages来进行交换。
        出现错误：javax.jms.JMSException: Failed to build body from content. Serializable class not available to broker.
         */
        ((ActiveMQConnection) connection).setTrustAllPackages(true);//暂时关闭安全检查，选择相信所有的包
        //((ActiveMQConnection) connection).setTrustedPackages(getTrustedPackages());//添加信任包在到trustAllPackages中


        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        //////////////注意这里JMSAppender只支持TopicDestination，下面会说到////////////////
        Destination topicDestination = session.createTopic("log4jTopic");
        MessageConsumer consumer = session.createConsumer(topicDestination);
        consumer.setMessageListener(message -> {
            try {
                // receive log event in your consumer

                LoggingEvent event = (LoggingEvent) ((ActiveMQObjectMessage) message).getObject();
                System.out.println("Received log [" + event.getLevel() + "]: " + event.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        TimeUnit.SECONDS.sleep(300);
        consumer.close();
        session.close();
        connection.close();
        System.exit(1);
    }

}
