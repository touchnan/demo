/*
 * cn.lab.xquery.JDBCXQuery.java
 * Sep 13, 2013 
 */
package cn.lab.xquery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.pool.OracleDataSource;

/**
 * Sep 13, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class JDBCXQuery {
    public static void main(String args[]) throws SQLException {
        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:hr/hr@localhost:1521/orcl11g");
        Connection conn = ods.getConnection();
        Statement stmt = conn.createStatement();
        String qry =
                "SELECT * FROM XMLTable(" + "'for $i in $h/employees/employee " + "return $i '"
                        + "PASSING xmlparse(document "
                        + "httpuritype('http://localhost/bonuses.xml').getCLOB()) as \"h\" "
                        + "COLUMNS ename VARCHAR2(45) PATH '/employee/ename', "
                        + "        bonus NUMBER(10,2) PATH '/employee/bonus' )";
        ResultSet rs = stmt.executeQuery(qry);
        System.out.println("Bonuses:");
        while (rs.next())
            System.out.println(rs.getString(1) + ": $" + rs.getFloat(2));
    }

}
