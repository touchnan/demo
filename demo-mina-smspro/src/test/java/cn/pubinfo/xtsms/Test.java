/*
 * cn.pubinfo.xtsms.Test.java
 * Jun 25, 2014 
 */
package cn.pubinfo.xtsms;

import cn.pubinfo.xtsms.sms.SmsDto;

import com.alibaba.fastjson.JSON;

/**
 * Jun 25, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        AtomicLong a = new AtomicLong(0);
//        System.out.println(a.getAndIncrement());
        
        SmsDto dto = new SmsDto();
        dto.setContent("a");
        System.out.println(JSON.toJSONString(dto, true));
    }

}
