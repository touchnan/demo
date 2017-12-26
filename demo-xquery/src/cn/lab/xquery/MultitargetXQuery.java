/*
 * cn.lab.xquery.MultitargetXQuery.java
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
public class MultitargetXQuery {
    public static void main (String args[]) throws SQLException
    {
     OracleDataSource ods = new OracleDataSource();
     ods.setURL("jdbc:oracle:thin:hr/hr@localhost:1521/orcl11g");
     Connection conn = ods.getConnection();
     Statement stmt = conn.createStatement();
     String qry = "SELECT d.department_name, b.ename, b.bonus FROM XMLTable("+
         "'for $i in $h/employees/employee " +
          "return $i '" +
          "PASSING xmlparse(document " +
          "httpuritype('http://localhost/bonuses.xml').getCLOB()) as \"h\" " +
          "COLUMNS empno NUMBER(6) PATH '/employee/empno', " +
          "        ename VARCHAR2(45) PATH '/employee/ename', " +
          "        bonus NUMBER(10,2) PATH '/employee/bonus' ) b," +
          "employees e," +
          "departments d " +
          "WHERE (e.employee_id = b.empno) AND (d.department_id=e.department_id)";
     ResultSet rs = stmt.executeQuery(qry);
     System.out.println("Bonuses:");
     while(rs.next())
          System.out.println("Department: " + rs.getString(1) +": " + rs.getString(2) + ": $" + rs.getFloat(3));
    }

}
