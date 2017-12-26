/*
 * cn.lab.xquery.XQJFileSource.java
 * Sep 13, 2013 
 */
package cn.lab.xquery;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

/**
 * Sep 13, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class XQJFileSource {
    public static void main(String args[]) throws Exception {
        XQDataSource xds = new oracle.xquery.xqj.OXQDataSource();
        XQConnection conn = xds.getConnection();
        XQPreparedExpression pEx =
                conn.prepareExpression("for $c in fn:doc('/home/myfiles/otntecharticle.xml')//item "
                        + "where fn:contains($c/title,'Oracle Database') " + "return $c/title");
        XQResultSequence rslt = pEx.executeQuery();
        while (rslt.next()) {
            System.out.println(rslt.getAtomicValue());
        }
        conn.close();
    }

}
