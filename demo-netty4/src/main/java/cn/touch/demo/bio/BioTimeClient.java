package cn.touch.demo.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Created by chengqiang.han on 2018/10/15.
 */
public class BioTimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {

            }
        }

//        Socket socket = null;
//        BufferedReader in = null;
//        PrintWriter out = null;

        try (Socket socket = new Socket("127.0.0.1", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(),true)) {

            interactive(in, out);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            interactive(in,out);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static void interactive(BufferedReader in, PrintWriter out) throws IOException {
        out.println("QUERY TIME ORDER");
        System.out.println("Send order 2 server succeed.");
        String resp = in.readLine();
        System.out.println("Now is : " + resp);
    }
}
