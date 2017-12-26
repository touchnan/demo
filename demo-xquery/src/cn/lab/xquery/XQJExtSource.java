/*
 * cn.lab.xquery.XQJExtSource.java
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
public class XQJExtSource {
    public static void main(String args[]) throws Exception {
        XQDataSource xds = new oracle.xquery.xqj.OXQDataSource();
        XQConnection conn = xds.getConnection();
        XQPreparedExpression pEx =
                conn.prepareExpression("declare variable $doc external;" + "for $c in $doc//item "
                        + "where fn:contains($c/title,'Oracle Database') " + "return $c/title");
        java.net.URL doc = new java.net.URL("http://feeds.delicious.com/v2/rss/OracleTechnologyNetwork/otntecharticle");
        java.io.InputStream inpt = doc.openStream();
        pEx.bindDocument(new javax.xml.namespace.QName("doc"), inpt, null, null);
        XQResultSequence rslt = pEx.executeQuery();
        while (rslt.next()) {
            System.out.println(rslt.getAtomicValue());
        }
        conn.close();
        inpt.close();
    }

}
