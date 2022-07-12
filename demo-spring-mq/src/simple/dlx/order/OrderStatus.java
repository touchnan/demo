package cn.lab.spring.demo.mq.dlx.order;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 */
public enum OrderStatus {
    UNPAY(3);


    int status;

    OrderStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
