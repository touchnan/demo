package cn.touch.demo.bigdata.client.zookeeper.curator.discovery;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/11/30.
 */
public class ServiceDetail {
    //服务注册的根路径
    public static final String REGISTER_ROOT_PATH = "/mall";

    private String desc;
    private int weight;

    public ServiceDetail() {}
    public ServiceDetail(String desc, int weight) {
        this.desc = desc;
        this.weight = weight;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    @Override
    public String toString() {
        return "ServiceDetail [desc=" + desc + ", weight=" + weight + "]";
    }
}
