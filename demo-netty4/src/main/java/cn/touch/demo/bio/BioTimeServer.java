package cn.touch.demo.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

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

            System.out.println("The time server is start in port : " + port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                System.out.println("accept!");
                new Thread(new Timerhander(socket)).start();
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
