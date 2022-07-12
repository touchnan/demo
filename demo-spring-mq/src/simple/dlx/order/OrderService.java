package cn.lab.spring.demo.mq.dlx.order;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 */
public interface OrderService {
    Order getByOrderId(String orderId);
    Integer updateOrderStatus(String orderId);
    String saveOrder(Order order);
}
