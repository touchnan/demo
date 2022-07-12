package cn.lab.spring.demo.mq.dlx.order;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 * rabbitmq死信队列 订单超时
 *
 * https://blog.csdn.net/weixin_39555954/article/details/120643241
 */

@Component
public class OrderDlxConfig {
    @Value(value="${xiaojie.order.queue}")
    private String orderQueue; //订单队列
    @Value(value="${xiaojie.order.exchange}")
    private String orderExchange;//订单队列
    @Value(value="${xiaojie.dlx.queue}")
    private String orderDeadQueue;//订单死信队列
    @Value(value="${xiaojie.dlx.exchange}")
    private String orderDeadExChange;//订单死信交换机
    @Value(value="${xiaojie.order.routingKey}")
    private String orderRoutingKey;//订单路由键
    @Value(value="${xiaojie.dlx.routingKey}")
    private String orderDeadRoutingKey;//死信队列路由键

    @Bean
    public Queue orderQueue(){
        Map<String, Object> args = new HashMap<>(2);
        // 绑定我们的死信交换机
        args.put("x-dead-letter-exchange", orderDeadExChange);
        // 绑定我们的路由key
        args.put("x-dead-letter-routing-key", orderDeadRoutingKey);
        return new Queue(orderQueue, true, false, false, args);
    }
    @Bean
    public Queue orderDeadQueue(){
        return new Queue(orderDeadQueue);
    }
    //绑定交换机
    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(orderExchange);
    }
    @Bean
    public DirectExchange orderDeadExchange(){
        return new DirectExchange(orderDeadExChange);
    }
    //绑定路由键
    @Bean
    public Binding orderBindingExchange(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(orderRoutingKey);
    }
    //绑定死信队列到死信交换机
    @Bean
    public Binding deadBindingExchange(Queue orderDeadQueue,  DirectExchange orderDeadExchange) {
        return BindingBuilder.bind(orderDeadQueue).to(orderDeadExchange).with(orderDeadRoutingKey);
    }
}
