package cn.touch.demo.jdkapi.bio;

/**
 * Created by chengqiang.han on 2018/10/16.
 */
public class ServerPort {
    protected static int getPort(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {

            }
        }
        return port;
    }
}
