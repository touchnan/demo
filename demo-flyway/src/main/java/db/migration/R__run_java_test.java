package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/14.
 *
 * 与 SQL 迁移不同，Java 迁移默认没有校验checksum ，因此不参与 Flyway 验证的变更检测。
 *  这可以通过实现该 getChecksum()方法来解决，然后您可以使用该方法提供您自己的校验和，然后将其存储并验证更改。
 *
 * 作者：三川三一宁
 * 链接：https://www.jianshu.com/p/f415d5925959
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class R__run_java_test extends BaseJavaMigration{
    public void migrate(Context context) {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("insert into account (userid,money) values (1,9000)");//"INSERT INTO test_user (name) VALUES ('Obelix')"
    }

    @Override
    public Integer getChecksum() {
        return 331450410;
    }
}
