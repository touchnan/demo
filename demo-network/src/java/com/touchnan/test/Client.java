/*
 * com.touchnan.test.Client.java
 * May 15, 2014 
 */
package com.touchnan.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * May 15, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Client {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8821);
            // 发送关闭命令
            OutputStream socketOut = socket.getOutputStream();
            for (int i=0; i<5;i++) {
                socketOut.write("shutdown".getBytes());
                socketOut.flush();
            }
            socketOut.write("shutdown".getBytes());
            socketOut.write("bye\r\n".getBytes());
            // 接收服务器的反馈
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String msg = null;
            while ((msg = br.readLine()) != null)
                System.out.println(msg);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
