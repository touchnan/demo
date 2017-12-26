package cn.touch.demo.os.event.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
 
/**
 * 我的Socket类.
 * 
 * 封装JDK内置Socket,提供收发消息等功能.
 * 
 * @author coombe
 */
public class MySocket {
    private Socket socket = null;
    private PrintStream printStream;
    private BufferedReader bufferedReader;
    private BufferedReader bufferedKeyReader;
    public static List<String> keyDownList = new ArrayList<String>();
 
    public MySocket() throws UnknownHostException, IOException {
        this("192.168.2.30", 45124);
    }
 
    public MySocket(Socket socket) {
        super();
        this.socket = socket;
    }
 
    public MySocket(String serverIp, int port) throws UnknownHostException,
            IOException {
        super();
        socket = new Socket(serverIp, port);
    }
 
    public void send(String msg) throws IOException {
        if (null == printStream) {
            printStream = new PrintStream(socket.getOutputStream());
        }
 
        printStream.println(msg);
    }
 
    public String receive() throws IOException {
        if (null == bufferedReader) {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
        }
 
        return bufferedReader.readLine();
    }
 
    public String readKey() throws IOException {
        if (keyDownList.size() != 0) {
            String s = keyDownList.get(0);
            keyDownList.remove(0);
            return s;
        }
        return null;
    }
 
    public String getIp() {
        return socket.getInetAddress().getHostAddress();
    }
 
    public void close() {
        if (null == socket)
            return;
 
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
