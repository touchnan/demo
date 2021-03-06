/*
 * cn.lab.xquery.ResolveExternalFunction2.java
 * Sep 13, 2013 
 */
package cn.lab.xquery;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQSequence;

import oracle.xquery.xqj.OXQConnection;
import oracle.xquery.xqj.OXQDataSource;

/**
 * Sep 13, 2013
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class ResolveExternalFunction2 {
    public static String trim(String value) {
        return value.trim();
    }
    
    private static class MyEntityResolver extends OXQEntityResolver {
        @Override
        public OXQEntity resolveEntity(OXQEntityKind kind, OXQEntityLocator locator,
                OXQEntityResolverRequestOptions options) throws XQException, IOException {
            if (kind == OXQEntityKind.EXTERNAL_FUNCTION) {
                OXQFunctionMetaData metaData = (OXQFunctionMetaData)locator.getExtension();
                QName name = metaData.getName();
                int arity = metaData.getParameterTypes().length;
                if ("http://example.com/util".equals(name.getNamespaceURI()) &&
                    "trim".equals(name.getLocalPart()) && arity == 1) {
                    Method staticMethod = null;
                    try {
                        staticMethod = ResolveExternalFunction2.class.getMethod("trim", String.class); 
                    } catch (NoSuchMethodException e) {
                        throw new IllegalStateException(e);
                    }
                    return new OXQEntity(staticMethod);
                }
            }
            return null;
        }
    }

    public static void main(String[] args) throws IOException, XQException {
        OXQDataSource ds = new OXQDataSource();
        XQConnection con = ds.getConnection();
        OXQConnection ocon = OXQView.getConnection(con);
        ocon.setEntityResolver(new MyEntityResolver());
 
        FileInputStream query = new FileInputStream("trim.xq");
        XQPreparedExpression expr = con.prepareExpression(query);
        query.close();
 
        XQSequence result = expr.executeQuery();
        System.out.println(result.getSequenceAsString(null));
 
        result.close();
        expr.close();
        con.close();
    }

}
