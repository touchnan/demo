package cn.touch.demo.embed;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/11/14.
 */
public class JettyLaunch {
    public static void main(String[] args) {
        Server server = new Server(8080);
        server.setHandler(new HelloWorld());

        server.start();
        server.join();
    }
}
