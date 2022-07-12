package cn.lab.spring.demo.mq.dlx.order;

import org.springframework.stereotype.Service;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 */
@Service
public class OrderMapper {
    public Integer addOrder(Order order) {
        return 1;
    }

    public Order getOrder(String orderId) {
        return new Order();
    }

    public Integer updateOrder(String orderId) {
        return 1;
    }
}
