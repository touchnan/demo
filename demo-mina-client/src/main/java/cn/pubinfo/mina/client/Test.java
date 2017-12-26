/*
 * cn.pubinfo.mina.client.Test.java
 * May 11, 2014 
 */
package cn.pubinfo.mina.client;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * May 11, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Test {
    private final static Logger log = LoggerFactory.getLogger(Test.class);
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        log.info("INSERT INTO `tab_test` (name) VALUES ('{}');","b");
        
        System.out.println(String.format(Locale.CHINESE, "%04d", new java.util.Random().nextInt(3000)));
        
        Client.getInstance().send("1111");
        //Client.getInstance().send("QUIT");
    }

}
