/*
 * cn.touchin.db.jdbc.OracleSQLParser.java
 * Mar 11, 2012 
 */
package cn.touchin.db.jdbc;

import java.util.List;

import cn.touchin.page.Pagination;

/**
 * Mar 11, 2012
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class OracleSQLParser extends SQLParser {

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.touchin.db.jdbc.ISQLParser#createSQLQuery(cn.touchin.db.jdbc.SQLRunner
     * )
     */
    @Override
    public SimpleSQLQuery createSQLQuery(SQLRunner runner) {
        StringBuffer sql = new StringBuffer("SELECT ");
        List<Object> params = createSql(runner, sql);
        Object[] queryParams = null;
        if (params != null && !params.isEmpty()) {
            queryParams = params.toArray(new Object[params.size()]);
        }
        return new SimpleSQLQuery(sql.toString(), queryParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.touchin.db.jdbc.ISQLParser#createPageQuery(cn.touchin.db.jdbc.SQLRunner
     * )
     */
    public PageQuery createPageQuery(SQLRunner runner) {
        StringBuffer sql = new StringBuffer(
                "SELECT * FROM ( SELECT row_.*, ROWNUM rownum_ FROM (SELECT ");
        StringBuffer countSql = new StringBuffer("SELECT (0)");
        List<Object> params = createPageSql(runner, sql, countSql);
        sql.append(" ) row_ WHERE ROWNUM <= ?) WHERE rownum_ > ?");
        Object[] pageParams = null;
        Object[] countParams = null;
        if (params != null && !params.isEmpty()) {
            countParams = params.toArray(new Object[params.size()]);
            pageParams = params.toArray(new Object[params.size() + 2]);
        } else {
            pageParams = new Object[2];
        }
        pageParams[pageParams.length - 2] = first(runner.flexiPage);
        pageParams[pageParams.length - 1] = second(runner.flexiPage);

        return new PageQuery(sql.toString(), pageParams, countSql.toString(),
                countParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cn.touchin.db.jdbc.ISQLParser#getSQLPageParams(cn.touchin.page.Pagination
     * )
     */
    public long[] getSQLPageParams(Pagination page) {
        return new long[] { first(page), second(page) };
    }

    private long first(Pagination page) {
        return page.getPage() * page.getRp();
    }

    private long second(Pagination page) {
        return (page.getPage() - 1) * page.getRp();
    }

}
