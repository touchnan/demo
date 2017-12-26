package cn.touch.demo.bigdata.client.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/9/14.
 */
public class MyProducer {
    /*
        http://kafka.apache.org/0100/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
     */
    public static final String TOPIC_NAME = "my-replicated-topic";

    public static void main(String[] args) {
        Properties props = new Properties();
        //props.put("bootstrap.servers", "192.168.1.79:9092,192.168.1.79:9093,192.168.1.79:9094");//kafka地址列表,多个地址用","号隔开
        
        props.put("bootstrap.servers", "192.168.1.79:9092");//kafka地址列表,多个地址用","号隔开

        
//        props.put("acks", "all");
//        props.put("retries", 0);
//        props.put("batch.size", 16384);
//        props.put("linger.ms", 1);
//        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for(int i = 0; i < 5; i++)
            producer.send(new ProducerRecord<String, String>(TOPIC_NAME, Integer.toString(i), Integer.toString(i)));

        producer.close();

    }
}
