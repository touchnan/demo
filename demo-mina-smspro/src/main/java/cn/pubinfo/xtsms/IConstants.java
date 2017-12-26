/*
 * cn.pubinfo.xtsms.IConstants.java
 * Jun 16, 2014 
 */
package cn.pubinfo.xtsms;

/**
 * Jun 16, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class IConstants {
    private static Config config = new Config("deploy");

    public final static int MINA_PORT = config.getInt("mina.port", 1101);

    public final static String JDBC_DRIVER = config.getString("jdbc.driverClassName", "com.mysql.jdbc.Driver");
    public final static String JDBC_URL =
            config.getString(
                    "jdbc.url",
                    "jdbc:mysql://localhost:3306/test?autoReconnect=true&amp;autoReconnectForPools=true&amp;useUnicode=true&amp;characterEncoding=utf8");
    public final static String JDBC_USER = config.getString("jdbc.user", "root");
    public final static String JDBC_PASSWD = config.getString("jdbc.passwd", "");

    public final static int THREAD_SEND_SLEEP_MILLISECONDS = config.getInt("thread.send.sleep.milliseconds", 500);
    public final static int DRAIN_SIZE = config.getInt("drain.size", 500);

    public final static int SEND_SUCC = 1;
    public final static int SEND_FAILED = 2;

    public final static String XJQ_SENDER_IP = config.getString("xjq.socket.ip", "localhost");
    public final static int XJQ_SENDER_PORT = config.getInt("xjq.socket.port", 1003);
}
