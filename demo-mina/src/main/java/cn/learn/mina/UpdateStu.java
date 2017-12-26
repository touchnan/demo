/*
 * cn.learn.mina.UpdateStu.java
 * Sep 24, 2013 
 */
package cn.learn.mina;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Sep 24, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class UpdateStu {
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static ResultSetMetaData rsmd; // ResultSetMetaData has not the close() method

    public static void main(String[] rgs) {
        Derby.loadDriver();// As is said in the class Derby, this action is not necessary with a JRE 1.6 and later.
        try {
            // create database "ch20"
            conn = Derby.createDatabaseAndGetConnection("ch20", "", "");

            // drop the table "tb_stu" if it exists
            if (Derby.isTableExists("tb_stu", conn)) {
                ps = conn.prepareStatement("drop table tb_stu");
                ps.execute();
            }

            // create a table "tb_stu" in this database
            ps =
                    conn.prepareStatement("create table tb_stu(id char(5), name varchar(20), sex varchar(10), birthday date)");
            ps.execute();

            // add elements into the table
            ps = conn.prepareStatement("insert into tb_stu values(?,?,?,?)");
            ps.setString(1, "01d05");
            ps.setString(2, "Jing LI");
            ps.setString(3, "female");
            ps.setDate(4, Date.valueOf("1999-10-20"));
            ps.executeUpdate();
            ps.setString(1, "01d06");
            ps.setString(2, "Hao LI");
            ps.setString(3, "male");
            ps.setDate(4, Date.valueOf("1988-10-20"));
            ps.executeUpdate();
            ps.setString(1, "01d15");
            ps.setString(2, "Yuanyuan XIONG");
            ps.setString(3, "female");
            ps.setDate(4, Date.valueOf("2001-10-20"));
            ps.executeUpdate();
            ps.setString(1, "01d16");
            ps.setString(2, "Mei LIU");
            ps.setString(3, "female");
            ps.setDate(4, Date.valueOf("1979-10-20"));
            ps.executeUpdate();

            // consult the information of all the students
            ps = conn.prepareStatement("select * from tb_stu");
            rs = ps.executeQuery();

            // display the query results on the console
            rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            String[] columnNames = new String[numberOfColumns];
            String[] columnTypeNames = new String[numberOfColumns];
            int[] precisions = new int[numberOfColumns];
            for (int i = 0; i < numberOfColumns; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
                columnTypeNames[i] = rsmd.getColumnTypeName(i + 1);
                precisions[i] = rsmd.getPrecision(i + 1);
                System.out.println(columnNames[i] + " : " + columnTypeNames[i] + "(" + precisions[i] + ")");
            }
            for (int i = 0; i < numberOfColumns; i++) {
                String columnName = columnNames[i];
                if (columnName.length() > precisions[i]) {
                    columnName = columnName.substring(0, precisions[i]);
                }
                System.out.printf("%-" + precisions[i] + "s|", columnName);
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 0; i < numberOfColumns; i++) {
                    System.out.printf("%-" + precisions[i] + "s|", rs.getObject(i + 1));
                }
                System.out.println();
            }

            // delete the students born before "2000-09-01"
            ps = conn.prepareStatement("delete from tb_stu where birthday<?");
            ps.setDate(1, Date.valueOf("2000-09-01"));
            ps.executeUpdate();

            // consult the information of all the students after the deletion
            System.out.println("The information of the rest students after the deletion operation:");
            ps = conn.prepareStatement("select * from tb_stu");
            rs = ps.executeQuery();

            // display the query results on the console
            for (int i = 0; i < numberOfColumns; i++) {
                String columnName = columnNames[i];
                if (columnName.length() > precisions[i]) {
                    columnName = columnName.substring(0, precisions[i]);
                }
                System.out.printf("%-" + precisions[i] + "s|", columnName);
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 0; i < numberOfColumns; i++) {
                    System.out.printf("%-" + precisions[i] + "s|", rs.getObject(i + 1));
                }
                System.out.println();
            }

            // close the opened resources
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // perform a clean database closing
            Derby.shutdownDatabase("ch20");
            Derby.shutdownAll();
        }
    }
}
