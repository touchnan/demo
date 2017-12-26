/*
 * cn.pubinfo.main.server.Main.java
 * May 11, 2014 
 */
package cn.pubinfo.main.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



/**
 * May 11, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Main {
    public final static BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
//    public final static ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<Object>();
//    public final static CopyOnWriteArrayList<Object> queue = new CopyOnWriteArrayList<Object>();
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        new LoopThread().start();
        Server.main(args);
    }

}
