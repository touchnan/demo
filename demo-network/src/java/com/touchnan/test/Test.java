/**
 * 
 */
package com.touchnan.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:touchnan@gmail.com">chengqiang.han</a>
 * 
 */
public class Test {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int numTasks = 20;

        ExecutorService exec = Executors.newCachedThreadPool();

        for (int i = 0; i < numTasks; i++) {
            exec.execute(createTask(i));
        }

        exec.awaitTermination(30, TimeUnit.MINUTES);

        // while(true) {
        // int i = System.in.read();
        // System.out.println((char)i);
        // if (i==120) {
        // break;
        // }
        // }
        // System.out.println("out");
    }

    // 定义一个简单的任务
    private static Runnable createTask(final int taskID) {
        return new Runnable() {
            private Socket socket = null;
            private int port = 8821;

            public void run() {
                System.out.println("Task " + taskID + ":start");
                try {
                    socket = new Socket("localhost", port);
                    // 发送关闭命令
                    OutputStream socketOut = socket.getOutputStream();
                    for (int i = 0; i < 5; i++) {
                        socketOut.write("shutdown".getBytes());
                        socketOut.flush();
                    }
                    socketOut.write("shutdown".getBytes());
                    socketOut.write("bye\r\n".getBytes());
                    // 接收服务器的反馈
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg = null;
                    while ((msg = br.readLine()) != null)
                        System.out.println(msg);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }
}
