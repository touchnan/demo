package cn.touch.demo.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chengqiang.han on 2018/10/15.
 */
public class BioTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {

            }
        }


        try (ServerSocket server = new ServerSocket(port);) {
            ExecutorService pool = Executors.newFixedThreadPool(10);
            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                System.out.println("accept!");
//                new Thread(new Timerhander(socket)).start();//每个客户端一个线程，则资源很快被耗尽

                /*- 看输出流的read api文档，数据读取阻塞，
                 当发送方应答消息比较慢或者传输较慢时，读取输入流导致线程长时间阻塞，则线程池中的线程被占用，
                 其他客户端接入消息只能在消息队列中排列，等待线程池处理;
                 而输出流write api文档，写输出流时也一样被阻塞，直到所有要发送的字节全部写入完毕或发生异常.
                 TCP/IP相关知识，当消息的接收处理缓慢时，将不能及时地从TCP缓冲区读取数据，这将导致发送方的
                 TCP WINDOWS SIZE不断减小，直到为0，双方处理Keep-Alive状态，消息发送方将不能再向TCP缓冲区写入消息。
                 这里如果采用的是同步阻塞I/O,write操作将会被无限期阻塞，直到TCP window size大于0或者发生I/O异常。

                 读和写都同步阻塞，阻塞时间取决于对方I/O线程的处理速度和网络I/O的传输速度

                 线程池这种伪异步仅仅只是简单优化，无法从根本解决同步I/O导致的通信线程阻塞问题
                */
                pool.submit(new Timerhander(socket));
            }
        } catch (IOException e) {

        }
    }

    static class Timerhander implements Runnable {
        private Socket socket;

        Timerhander(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (//Socket socket = this.socket;
                 BufferedReader in = new BufferedReader((new InputStreamReader(socket.getInputStream())));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
                String currentTime = null;
                String body = null;
                while (true) {
                    body = in.readLine();
                    if (body == null) {
                        break;
                    }
                    System.out.println("The time server receive order:  " + body);
                    currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    out.println(currentTime);
                }
            } catch (IOException e) {
            }
        }
    }

}
