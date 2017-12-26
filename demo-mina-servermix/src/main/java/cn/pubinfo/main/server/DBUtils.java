/*
 * cn.pubinfo.main.server.DBUtils.java
 * May 22, 2014 
 */
package cn.pubinfo.main.server;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * May 22, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class DBUtils {
    private static Logger log = LoggerFactory.getLogger(DBUtils.class);
    private ComboPooledDataSource c3p0;
    private QueryRunner queryRunner;
    private DBUtils() {
        super();
        c3p0 = new ComboPooledDataSource();
        try {
            c3p0.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            log.error("%s",e);
        }
        c3p0.setUser("root");
        c3p0.setPassword("");
        c3p0.setJdbcUrl("jdbc:mysql://localhost:3306/mydb1");
        queryRunner = new QueryRunner(c3p0);
    }

    public static DBUtils getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final DBUtils instance = new DBUtils();
    }

    public int[] batchUpdate(String sql, Object[][] params) {
        int[] affectedRows = new int[0];
        try {
            affectedRows = queryRunner.batch(sql, params);
        } catch (SQLException e) {
            log.error("%s",e);
        }
        return affectedRows;
    }
    
    public int update(String sql, Object... params) {
        // queryRunner = new QueryRunner(dataSource);
        int affectedRows = 0;
        try {
            if (params == null) {
                affectedRows = queryRunner.update(sql);
            } else {
                affectedRows = queryRunner.update(sql, params);
            }
        } catch (SQLException e) {
            log.error("%s",e);
        }
        return affectedRows;
    }    
    
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        c3p0.close();
        super.finalize();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DBUtils db = DBUtils.getInstance();
//        db.update("DROP  TABLE IF EXISTS `tab_m`");
//        db.update("CREATE  TABLE `tab_m` (`id` INT NOT NULL AUTO_INCREMENT,`col_msg` VARCHAR(150) NOT NULL, `col_phone` VARCHAR(18),PRIMARY KEY (`id`) )");
        
//        db.update("INSERT INTO `tab_m` (`id`, `col_msg`, `col_phone`) VALUES (1, '从来不清楚测试的信息', '18912345678')");
        
//        db.update("INSERT INTO `tab_m` (`col_msg`, `col_phone`) VALUES ( ?, ?)",new Object[]{"从来不清楚测试的信息","18912345678"});
        
        int row = 500;
        Date now = new Date();
        Object[][] params = new Object[row][2];
        for (int i=0; i<row; i++) {
            params[i] = new Object[]{"从来不清楚测试的信息"+i,"18912345678",now};
        }
        System.out.println(new Date());
        //db.batchUpdate("INSERT INTO `tab_m` (`col_msg`, `col_phone`) VALUES ( ?, ?)",params);
        
        db.batchUpdate("INSERT INTO `smsreport` (`content`, `sendto`, `sendtime`) VALUES ( ?, ?, ?)",params);
        System.out.println(new Date());
    }

}
