package cn.lab.spring.demo.mq.dlx;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 *
 * 消费snail消息的消费者
 */
@Component
//@Slf4j
public class SnailConsumer {
    @RabbitListener(queues = "snail_direct_queue")
    public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        // 获取消息Id
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "UTF-8");
//        log.info("获取到的消息>>>>>>>{},消息id>>>>>>{}", msg, messageId);
        System.out.println(String.format("获取到的消息>>>>>>>{},消息id>>>>>>{}", msg, messageId));
        try {
            int result = 1 / 0;
            System.out.println("result" + result);
            // // 手动ack
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            // 手动签收
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            //拒绝消费消息（丢失消息） 给死信队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
