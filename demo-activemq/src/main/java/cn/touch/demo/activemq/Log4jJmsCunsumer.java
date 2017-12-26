package cn.touch.demo.activemq;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.spi.LoggingEvent;

import java.util.concurrent.TimeUnit;

public class Log4jJmsCunsumer {

	public static void main(String[] args) throws JMSException, InterruptedException {
		// TODO Auto-generated method stub
		ActiveMQConnectionFactory factory = 
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        //////////////注意这里JMSAppender只支持TopicDestination，下面会说到////////////////
        Destination topicDestination = session.createTopic("log4jTopic");
        MessageConsumer consumer = session.createConsumer(topicDestination);
        consumer.setMessageListener(message -> {
            try {
                // receive log event in your consumer
//                LoggingEvent event = (LoggingEvent)((ActiveMQObjectMessage)message).getObject();
//                System.out.println("Received log [" + event.getLevel() + "]: "+ event.getMessage());
            	System.out.println(((ActiveMQObjectMessage)message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        TimeUnit.MILLISECONDS.sleep(3000000);
        consumer.close();
        session.close();
        connection.close();
        System.exit(1);
	}

}
