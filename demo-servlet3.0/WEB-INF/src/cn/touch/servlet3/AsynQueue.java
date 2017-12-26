/*
 * cn.touch.servlet3.AsynQueue.java
 * Jan 25, 2014 
 */
package cn.touch.servlet3;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;


/**
 * Jan 25, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class AsynQueue implements Runnable{
    private static java.util.Queue<AsyncContext> queue = new LinkedBlockingQueue<AsyncContext>();
    
    public static void put(AsyncContext obj) {
        //AsyncContext ac
        queue.add(obj);
    }
    
    public static Object get() {
        return queue.poll();
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // TODO Auto-generated method stub
        int i=0;
        while (true) {
            AsyncContext ac = queue.poll();
            if (ac ==null) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            }
            i++;
            try {
                ac.getResponse().getWriter().append("test for"+i).flush();
                ac.complete();
            } catch (IOException e) {
            }
        }
    }
    
    
}
