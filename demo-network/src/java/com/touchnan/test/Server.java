/*
 * com.touchnan.test.Server.java
 * May 15, 2014 
 */
package com.touchnan.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * May 15, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Server {
    private static  byte[] buf = new byte[2014];
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8821);
        Socket socket;
        while(true) {
            socket = ss.accept();
            System.out.print(socket);
//            InputStream in = socket.getInputStream();
//            int i=-1;
//            while((i=in.read(buf))!=-1) {
//                for (int j =0; j<i; j++) {
//                    System.out.println((char)buf[j]);
//                }
//            }
        }
    }
}
