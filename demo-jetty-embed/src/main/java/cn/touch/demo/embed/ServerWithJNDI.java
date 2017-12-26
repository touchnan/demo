package cn.touch.demo.embed;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/11/14.
 */
/*-
http://www.eclipse.org/jetty/documentation/9.3.x/jndi-embedded.html


In addition to the jars that you require for your application, and the jars needed for core Jetty, you will need to place the following jars onto your classpath:
jetty-jndi.jar
jetty-plus.jar
 */
public class ServerWithJNDI {
    public static void main(String[] args) {
        //Create the server
        Server server = new Server(8080);

        //Enable parsing of jndi-related parts of web.xml and jetty-env.xml
        org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(server);
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");

        //Create a WebApp
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("./my-foo-webapp.war");
        server.setHandler(webapp);

        //Register new transaction manager in JNDI
        //At runtime, the webapp accesses this as java:comp/UserTransaction
        org.eclipse.jetty.plus.jndi.Transaction transactionMgr = new org.eclipse.jetty.plus.jndi.Transaction(new com.acme.MockUserTransaction());

        //Define an env entry with Server scope.
        //At runtime, the webapp accesses this as java:comp/env/woggle
        //This is equivalent to putting an env-entry in web.xml:
        //<env-entry>
        //  <env-entry-name>woggle</env-entry-name>
        //  <env-entry-type>java.lang.Integer</env-entry-type>
        //  <env-entry-value>4000</env-entry-value>
        //</env-entry>
        org.eclipse.jetty.plus.jndi.EnvEntry woggle = new org.eclipse.jetty.plus.jndi.EnvEntry(server, "woggle", new Integer(4000), false);


        //Define an env entry with webapp scope.
        //At runtime, the webapp accesses this as java:comp/env/wiggle
        //This is equivalent to putting a web.xml entry in web.xml:
        //<env-entry>
        //  <env-entry-name>wiggle</env-entry-name>
        //  <env-entry-value>100</env-entry-value>
        //  <env-entry-type>java.lang.Double</env-entry-type>
        //</env-entry>
        //Note that the last arg of "true" means that this definition for "wiggle" would override an entry of the
        //same name in web.xml
        org.eclipse.jetty.plus.jndi.EnvEntry wiggle = new org.eclipse.jetty.plus.jndi.EnvEntry(webapp, "wiggle", new Double(100), true);

        //Register a reference to a mail service scoped to the webapp.
        //This must be linked to the webapp by an entry in web.xml:
        // <resource-ref>
        //  <res-ref-name>mail/Session</res-ref-name>
        //  <res-type>javax.mail.Session</res-type>
        //  <res-auth>Container</res-auth>
        // </resource-ref>
        //At runtime the webapp accesses this as java:comp/env/mail/Session
        org.eclipse.jetty.jndi.factories.MailSessionReference mailref = new org.eclipse.jetty.jndi.factories.MailSessionReference();
        mailref.setUser("CHANGE-ME");
        mailref.setPassword("CHANGE-ME");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.host","CHANGE-ME");
        props.put("mail.from","CHANGE-ME");
        props.put("mail.debug", "false");
        mailref.setProperties(props);
        org.eclipse.jetty.plus.jndi.Resource xxxmail = new org.eclipse.jetty.plus.jndi.Resource(webapp, "mail/Session", mailref);


        // Register a  mock DataSource scoped to the webapp
        //This must be linked to the webapp via an entry in web.xml:
        //<resource-ref>
        //  <res-ref-name>jdbc/mydatasource</res-ref-name>
        //  <res-type>javax.sql.DataSource</res-type>
        //  <res-auth>Container</res-auth>
        //</resource-ref>
        //At runtime the webapp accesses this as java:comp/env/jdbc/mydatasource
        org.eclipse.jetty.plus.jndi.Resource mydatasource = new org.eclipse.jetty.plus.jndi.Resource(webapp, "jdbc/mydatasource",
                new com.acme.MockDataSource());

        server.start();
        server.join();
    }
}
