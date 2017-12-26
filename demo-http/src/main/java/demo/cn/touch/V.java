/*
 * demo.cn.touch.V.java
 * May 11, 2014 
 */
package demo.cn.touch;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * May 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class V {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //SynchronousQueue queue =  new SynchronousQueue<Runnable>();
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(300000);
        
//        ExecutorService executor =
//            new ThreadPoolExecutor(1, 100, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
//        ExecutorService executor = Executors.newCachedThreadPool();
        ExecutorService executor =
            new ThreadPoolExecutor(1, 500, 60L, TimeUnit.SECONDS, queue);
        final HttpConnector conn = new HttpConnector();
        for (int i = 300000; i > 0; i--) {
//            queue.add();
            executor.execute(new Runnable() {
                public void run() {
                    conn.get("http://localhost:8080/client/client");
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);

        
    }

}
