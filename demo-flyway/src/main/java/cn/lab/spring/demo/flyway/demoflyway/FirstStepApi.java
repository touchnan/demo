package cn.lab.spring.demo.flyway.demoflyway;

import org.flywaydb.core.Flyway;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/14.
 *
 * H2-Table CATALOGS not found : JDBC URL 后面拼接参数 ;OLD_INFORMATION_SCHEMA=TRUE
 */
public class FirstStepApi {
    public static void main(String[] args) {
        // Create the Flyway instance and point it to the database
        Flyway flyway = Flyway.configure().locations("classpath:h2/migration").dataSource("jdbc:h2:file:./target/foobar", "sa", null).load();

        // Start the migration
        flyway.migrate();
    }
}
