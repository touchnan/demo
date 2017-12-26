/*
 * cn.pubinfo.main.server.DBUtils.java
 * May 22, 2014 
 */
package cn.pubinfo.xtsms.serv;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pubinfo.xtsms.IConstants;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * May 22, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class DB {
    private static Logger log = LoggerFactory.getLogger(DB.class);
    private ComboPooledDataSource c3p0;
    private QueryRunner queryRunner;

    public DB() {
        super();
        init();
    }

    private void init() {
        c3p0 = new ComboPooledDataSource();
        try {
            c3p0.setDriverClass(IConstants.JDBC_DRIVER);
        } catch (PropertyVetoException e) {
            log.error("%s", e);
        }
        c3p0.setUser(IConstants.JDBC_USER);
        c3p0.setPassword(IConstants.JDBC_PASSWD);
        c3p0.setJdbcUrl(IConstants.JDBC_URL);
        queryRunner = new QueryRunner(c3p0);
    }

    // /**
    // * {@inheritDoc}
    // */
    // public QueryRunner getQueryRunner() {
    // return queryRunner;
    // }

    // public static DBUtils getInstance() {
    // return Holder.instance;
    // }
    //
    // private static class Holder {
    // private static final DBUtils instance = new DBUtils();
    // }

    // /**
    // * {@inheritDoc}
    // */
    // public List<Map<String, Object>> find(String sql, Object... params) {
    // // queryRunner = new QueryRunner(dataSource);
    // List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    // try {
    // if (params == null) {
    // list = (List<Map<String, Object>>) queryRunner.query(sql, new MapListHandler());
    // } else {
    // list = (List<Map<String, Object>>) queryRunner.query(sql, new MapListHandler(), params);
    // }
    // } catch (SQLException e) {
    // throw new RuntimeException(e);
    // }
    // return list;
    // }

    // /**
    // * {@inheritDoc}
    // */
    // public <T> List<T> find(Class<T> entityClass, String sql, Object... params) {
    // // queryRunner = new QueryRunner(dataSource);
    // List<T> list = new ArrayList<T>();
    // try {
    // if (params == null) {
    // list = (List<T>) queryRunner.query(sql, new BeanListHandler<T>(entityClass));
    // } else {
    // list = (List<T>) queryRunner.query(sql, new BeanListHandler<T>(entityClass), params);
    // }
    // } catch (SQLException e) {
    // throw new RuntimeException(e);
    // }
    // return list;
    // }

    public <T> List<T> find(String sql, ResultSetHandler<List<T>> rsh, Object... params) {
        try {
            return queryRunner.query(sql, rsh, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public <T> T find2(String sql, ResultSetHandler<T> rsh, Object... params) {
        try {
            return queryRunner.query(sql, rsh, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }    

    public int[] batchUpdate(String sql, Object[][] params) {
        int[] affectedRows = new int[0];
        try {
            affectedRows = queryRunner.batch(sql, params);
        } catch (SQLException e) {
            log.error("%s", e);
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
            log.error("%s", e);
        }
        return affectedRows;
    }

    public void shutdown() {
        if (c3p0 != null) {
            c3p0.close();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        DB db = new DB();
        // db.update("DROP  TABLE IF EXISTS `tab_m`");
        // db.update("CREATE  TABLE `tab_m` (`id` INT NOT NULL AUTO_INCREMENT,`col_msg` VARCHAR(150) NOT NULL, `col_phone` VARCHAR(18),PRIMARY KEY (`id`) )");

        // db.update("INSERT INTO `tab_m` (`id`, `col_msg`, `col_phone`) VALUES (1, '从来不清楚测试的信息', '18912345678')");

        // db.update("INSERT INTO `tab_m` (`col_msg`, `col_phone`) VALUES ( ?, ?)",new
        // Object[]{"从来不清楚测试的信息","18912345678"});

        int row = 500;
        Date now = new Date();
        Object[][] params = new Object[row][2];
        for (int i = 0; i < row; i++) {
            params[i] = new Object[] { "从来不清楚测试的信息" + i, "18912345678", now };
        }
        System.out.println(new Date());
        // db.batchUpdate("INSERT INTO `tab_m` (`col_msg`, `col_phone`) VALUES ( ?, ?)",params);

        db.batchUpdate("INSERT INTO `smsreport` (`content`, `sendto`, `sendtime`) VALUES ( ?, ?, ?)", params);
        System.out.println(new Date());
    }

}
