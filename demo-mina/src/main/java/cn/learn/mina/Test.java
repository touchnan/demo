/*
 * cn.learn.mina.Test.java
 * Sep 16, 2013 
 */
package cn.learn.mina;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sep 16, 2013
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Test {
    private static final AtomicInteger id = new AtomicInteger();
    private static final Semaphore lock = new Semaphore(1);
    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        ExecutorService e = Executors.newFixedThreadPool(100);
        for (int i=0; i<=5000;i++) {
            e.execute(new Runnable() {
                public void run() {
                    try {
                        lock.acquire();
                        int i=id.incrementAndGet();
                        System.out.println(i);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        lock.release();
                    }
                    
                }
            });
        }
        e.shutdown();
        System.out.println("aaaaa");
//        e.awaitTermination(5, TimeUnit.SECONDS);
//        e.awaitTermination(1, TimeUnit.SECONDS);
       
//        ExecutorService serv = Executors.newFixedThreadPool(100);
//        serv
    }

}
