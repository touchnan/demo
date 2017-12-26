package cn.touch.demo.os.event.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
 
public class KeyOutServer {
    public static void main(String[] args) {
        ServerSocket ss = null;
        MySocket mySocket = null;
 
        try {
            ss = new ServerSocket(45124);
            while (true) {
                Socket socket = ss.accept();
                mySocket = new MySocket(socket);
                Service s = new Service(mySocket);
                Thread t = new Thread(s);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != ss) {
                    ss.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
 
            mySocket.close();
        }
    }
}
 
class Service implements Runnable {
    private MySocket mySocket;
    private static List<MySocket> clientList = new ArrayList<MySocket>();
    private String ip;
 
    public Service(MySocket mySocket) {
        super();
        this.mySocket = mySocket;
        clientList.add(mySocket);
        ip = mySocket.getIp();
 
        try {
            MySocket client;
            int count = clientList.size();
            for (int i = 0; i < count; i++) {
                client = clientList.get(i);
                client.send("已连接到控制端：" + ip);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void run() {
        String line = null;
        try {
            while (true) {
                line = mySocket.receive();
                if (null == line || line.length() == 0)
                    continue;
 
                MySocket client=null;
                int count = clientList.size();
                for (int i = 0; i < count; i++) {
                    client = clientList.get(i);
                    String[] t = line.split(":");
                    if("13".equals(t[1])){
                        continue;
                    }
                    switch (new Integer(t[0])) {
                    case 0:
                        RobotUtil.sendKey(new Integer(line.split(":")[1]));
                        break;
                    case 1:
                        RobotUtil.sendKeyUp(new Integer(line.split(":")[1]));
                        break;
                    case 2:
                        RobotUtil.sendKeyDown(new Integer(line.split(":")[1]));
                        break;
                    default:
                        break;
                    }
 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
