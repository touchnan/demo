/*
 * cn.lab.xquery.XMLIndexXQuery.java
 * Sep 13, 2013 
 */
package cn.lab.xquery;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.pool.OracleDataSource;

/**
 * Sep 13, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class XMLIndexXQuery {
    public static void main(String args[]) throws SQLException {
        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:usr/usr@localhost:1521/orcl11g");
        Connection conn = ods.getConnection();
        Statement stmt = conn.createStatement();
        String qry =
                "INSERT INTO otn_xml(item, link) "
                        + "SELECT * FROM XMLTable("
                        + "'for  $i in $h/rss/channel/item "
                        + "return {$i/title, $i/link, $i/pubDate, $i/description}' "
                        + "PASSING xmlparse (document httpuritype                             ('http://feeds.delicious.com/v2/rss/OracleTechnologyNetwork/otntecharticle').getCLOB()) as \"h\" "
                        + "COLUMNS item XMLType PATH '/', " + "link VARCHAR2(200) PATH '/item/link') "
                        + "WHERE link NOT IN (SELECT link FROM otn_xml)";
        int rows = stmt.executeUpdate(qry);
        conn.commit();
        System.out.println("Number of rows inserted now is: " + rows);
    }
}
