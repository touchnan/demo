package cn.touch.demo.jdkapi.bio;

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
public class BioTimeClient extends ServerPort {
    public static void main(String[] args) {
        int port = getPort(args);

//        Socket socket = null;
//        BufferedReader in = null;
//        PrintWriter out = null;

        try (Socket socket = new Socket("127.0.0.1", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(),true)) {

            interactive(in, out);

            sleeping(5);

            interactive(in,out);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static void sleeping(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void interactive(BufferedReader in, PrintWriter out) throws IOException {
        out.println("QUERY TIME ORDER");
        System.out.println("Send order 2 server succeed.");
        String resp = in.readLine();
        System.out.println("Now is : " + resp);
    }
}
