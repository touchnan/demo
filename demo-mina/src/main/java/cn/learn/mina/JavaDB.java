/*
 * cn.learn.mina.JavaDB.java
 * Sep 18, 2013 
 */
package cn.learn.mina;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sep 18, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class JavaDB {

    private static String connectionURL = "jdbc:derby:testdb2;create=true";
    private static Connection conn = null;
    static {
        try {
            conn = DriverManager.getConnection(connectionURL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        JavaDB db = new JavaDB();
        db.initTable();
        db.query();
        conn.close(); // alternative: shutdown the database.
    }

    public void initTable() throws SQLException {
        Statement stmt = conn.createStatement();
        if (Derby.isTableExists("tab_u", conn)) {
            stmt.execute("DROP TABLE tab_u");
        }
        stmt.execute("CREATE TABLE tab_u(id INTEGER NOT NULL PRIMARY KEY,name VARCHAR(45),state INTEGER)");
        stmt.close();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO tab_u (id, name, state) VALUES (?, '红', 0)");
        for (int i = 1; i < 100; i++) {
            ps.setObject(1, i);
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

    public void query() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tab_u");
        while (rs.next()) {
            System.out.println(rs.getInt("ID") + "=" + rs.getString("NAME"));
        }
    }
}
