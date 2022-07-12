package cn.lab.spring.demo.mq.dlx;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 *
 * 死信队列配置
 *
 */
public class DLXConfig {
    //定义队列
    private static final String MY_DIRECT_QUEUE = "snail_direct_queue";
    //定义队列
    private static final String MY_DIRECT_DLX_QUEUE = "xiaojie_direct_dlx_queue";
    //定义死信交换机
    private static final String MY_DIRECT_DLX_EXCHANGE = "xiaojie_direct_dlx_exchange";
    //定义交换机
    private static final String MY_DIRECT_EXCHANGE = "snail_direct_exchange";
    //死信路由键
    private static final String DIRECT_DLX_ROUTING_KEY = "msg.dlx";

    public Queue dlxQueue() {
        return new Queue(MY_DIRECT_DLX_QUEUE);
    }
    //绑定死信交换机
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(MY_DIRECT_DLX_EXCHANGE);
    }

//    @Bean
//    public Queue snailQueue() {
//        Map<String, Object> args = new HashMap<>(2);
//        // 绑定我们的死信交换机
//        args.put("x-dead-letter-exchange", MY_DIRECT_DLX_EXCHANGE);
//        // 绑定我们的路由key
//        args.put("x-dead-letter-routing-key", DIRECT_DLX_ROUTING_KEY);
//        return new Queue(MY_DIRECT_QUEUE, true, false, false, args);
//    }
    @Bean
    public Queue snailQueue() {
        Map<String, Object> args = new HashMap<>(2);
        // 绑定我们的死信交换机
        args.put("x-dead-letter-exchange", MY_DIRECT_DLX_EXCHANGE);
        // 绑定我们的路由key
        args.put("x-dead-letter-routing-key", DIRECT_DLX_ROUTING_KEY);
//        args.put("x-message-ttl", 5000); //为队列设置过期时间
//        x-max-length:队列最大容纳消息条数，大于该值，mq拒绝接受消息，消息进入死信队列
        args.put("x-max-length", 5);
        /** 注意：如果在添加了这一条（队列长度）发生异常时，请删除掉交换机和队列后，重新启动程序，重新进行绑定。 **/
        return new Queue(MY_DIRECT_QUEUE, true, false, false, args);
    }


    @Bean
    public DirectExchange snailExchange() {
        return new DirectExchange(MY_DIRECT_EXCHANGE);
    }
    //绑定队列到交换机
    @Bean
    public Binding snailBindingExchange(Queue snailQueue, DirectExchange snailExchange) {
        return BindingBuilder.bind(snailQueue).to(snailExchange).with("msg.send");
    }
    //绑定死信队列到死信交换机
    @Bean
    public Binding dlxBindingExchange(Queue dlxQueue, DirectExchange dlxExchange) {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(DIRECT_DLX_ROUTING_KEY);
    }

}
