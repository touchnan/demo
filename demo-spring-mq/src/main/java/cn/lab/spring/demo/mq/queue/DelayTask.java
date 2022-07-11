package cn.lab.spring.demo.mq.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/11.
 */
public class DelayTask implements Delayed {
    private String msg;
    private long executeTime;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public DelayTask(long delayTime, String msg){
        //到期时间 = 当前时间 + 延迟时间
        this.executeTime = System.currentTimeMillis() + delayTime;
        this.msg = msg;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.executeTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int)(this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }
}
