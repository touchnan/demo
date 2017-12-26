/*
 * cn.lab.xquery.XQueryPragmas.java
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
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class XQueryPragmas {
    public static void main (String args[]) throws SQLException
    {
     OracleDataSource ods = new OracleDataSource();
     ods.setURL("jdbc:oracle:thin:oe/oe@localhost:1521/orcl11g");
     Connection conn = ods.getConnection();
     Statement stmt = conn.createStatement();
     String qry = "SELECT e.ename, po.reference FROM XMLTable("+
         "'for $i in $h/employees/employee " +
          "return $i '" +
          "PASSING xmlparse(document " +
          "httpuritype('http://localhost/bonuses.xml').getCLOB()) as \"h\" " +
          "COLUMNS ename VARCHAR2(25) PATH '/employee/ename', " +
          "        email VARCHAR2(25) PATH '/employee/email') e, " +
          "purchaseorder p, " +
          "XMLTable(" +
          "'for $i in $po/PurchaseOrder " +
          "return $i ' " +
          "PASSING OBJECT_VALUE as \"po\" " +
          "COLUMNS reference VARCHAR2(30) PATH '/PurchaseOrder/Reference', " +
          "        usr VARCHAR2(25) PATH '/PurchaseOrder/User', " +
          "        orddt VARCHAR2(25) PATH '/PurchaseOrder/Date') po " +
        "WHERE po.usr = e.email " +
        "ORDER BY po.orddt"; 
     ResultSet rs = stmt.executeQuery(qry);
     while(rs.next())
          System.out.println("User: " + rs.getString(1) + "PORef" + rs.getString(2));
    }

}
