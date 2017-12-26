package cn.touch.demo.log4j.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/9/14.
 */
public class MyConsumerAutomaticOffsetCommitting {
    /*
        http://kafka.apache.org/0100/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html
     */
    public static void main(String[] args) {

        Properties props = new Properties();
//        props.put("bootstrap.servers", "192.168.1.79:9092,192.168.1.79:9093,192.168.1.79:9094");//kafka地址列表,多个地址用","号隔开
        props.put("bootstrap.servers", "192.168.1.79:9092");//kafka地址列表,多个地址用","号隔开
        props.put("group.id", "test_auto");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("my-replicated-topic"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
            	System.out.println();
            }
        }
    }
}
