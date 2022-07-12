package cn.lab.spring.demo.mq.dlx.order;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/12.
 */
public class Order {
        private String orderId;
        private String orderName;
        private Double payMoney;

        private int status;

        public String getOrderId() {
                return orderId;
        }

        public void setOrderId(String orderId) {
                this.orderId = orderId;
        }

        public String getOrderName() {
                return orderName;
        }

        public void setOrderName(String orderName) {
                this.orderName = orderName;
        }

        public Double getPayMoney() {
                return payMoney;
        }

        public void setPayMoney(Double payMoney) {
                this.payMoney = payMoney;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }
}
