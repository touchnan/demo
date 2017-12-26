package cn.touch.demo.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

public class JmsSender {
	public static void main(String[] args) throws JMSException {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
		// JMS 客户端到JMS Provider 的连接
		Connection connection = connectionFactory.createConnection();
		connection.start();
		// Session： 一个发送或接收消息的线程
		Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		// Destination ：消息的目的地;消息发送给谁.
		// 获取session注意参数值my-queue是Query的名字
		Destination destination = session.createQueue("my-queue");
		// MessageProducer：消息生产者
		MessageProducer producer = session.createProducer(destination);
		// 设置不持久化
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		// 发送一条消息
		sendMsg(session, producer);
		session.commit();
		connection.close();
	}

	/**
	 * 在指定的会话上，通过指定的消息生产者发出一条消息
	 * 
	 * @param session
	 *            消息会话
	 * @param producer
	 *            消息生产者
	 */
	public static void sendMsg(Session session, MessageProducer producer) throws JMSException {
		// 创建一条文本消息
		TextMessage message = session.createTextMessage("Hello ActiveMQ！");
		
		long delay = 10 * 1000;
		message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
		
		// 通过消息生产者发出消息
		producer.send(message);
		System.out.println("");
	}
}
