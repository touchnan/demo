package cn.touch.jdk8;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by touchnan on 2015/12/9.
 */
public class Main {
    public static void main(String[] args) {
        Executor exec = Executors.newFixedThreadPool(1);
        exec.execute(() -> System.out.print("Hello jdk8!"));
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Arrays.asList(1,2,3,4,5).forEach(e -> System.out.println("e = " + e));

        System.exit(0);
    }
}
