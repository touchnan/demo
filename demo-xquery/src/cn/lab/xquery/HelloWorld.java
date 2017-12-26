/*
 * cn.lab.xquery.HelloWorld.java
 * Sep 13, 2013 
 */
package cn.lab.xquery;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQSequence;

import oracle.xquery.xqj.OXQDataSource;

/**
 * Sep 13, 2013
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class HelloWorld {

    /**
     * @param args
     * @throws XQException 
     */
    public static void main(String[] args) throws XQException {
        OXQDataSource ds = new OXQDataSource();
        XQConnection con = ds.getConnection();
        String query = "<hello-world>{1 + 1}</hello-world>";
        XQPreparedExpression expr = con.prepareExpression(query); 
        XQSequence result = expr.executeQuery();
        System.out.println(result.getSequenceAsString(null));
        
        result.close();
        expr.close();
        con.close();
    }

}
