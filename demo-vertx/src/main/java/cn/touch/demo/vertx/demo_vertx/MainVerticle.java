package cn.touch.demo.vertx.demo_vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.sql.*;
import java.util.List;

public class MainVerticle extends AbstractVerticle {


    private Connection conn;

    private String db_username = "test";
    private String db_passwd = "password";
    private String db_url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&allowMultiQueries=true";

    private ResultSet resultSet;
    private int idx = 0;

    private ResultSet resultSet1;
    private int idx1 = 0;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        start1(startPromise);
        start2(startPromise);


    }

    private void initData() {
        try {
            if (resultSet==null || resultSet1 == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");

                conn = DriverManager.getConnection(db_url, db_username, db_passwd);

                PreparedStatement preparedStatement = conn.prepareStatement("select * from a_caselibrary");
                resultSet = preparedStatement.executeQuery();

                PreparedStatement preparedStatement1 = conn.prepareStatement("select * from a_problembasedetailed");
                resultSet1 = preparedStatement1.executeQuery();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    private void start1(Promise<Void> startPromise) throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            System.out.printf("req");
            try {
                initData();
                while (resultSet.next()) {
                    idx ++;
                    System.err.println(resultSet.getObject(1));
                    if (idx % 10 == 0) {
                        break;
                    }
                };
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        }).listen(8888, http -> {
            JsonObject config = this.context.config();
            List<String> strings = this.processArgs();
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    private void start2(Promise<Void> startPromise) throws Exception {
        // Create a Router
        Router router = Router.router(vertx);

        // Mount the handler for all incoming requests at every path and HTTP method
        router.route().handler(context -> {
            // Get the address of the request
            String address = context.request().connection().remoteAddress().toString();
            // Get the query parameter "name"
            MultiMap queryParams = context.queryParams();
            String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
            initData();
            try {
                while (resultSet1.next()) {
                    idx1 ++;
                    System.err.println(resultSet1.getObject(1));
                    if (idx1 % 10 == 0) {
                        break;
                    }
                };
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Write a json response
            context.json(
                    new JsonObject()
                            .put("name", name)
                            .put("address", address)
                            .put("message", "Hello " + name + " connected from " + address)
            );
        });

        // Create the HTTP server
        vertx.createHttpServer()
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(8889)
                // Print the port
                .onSuccess(server ->
                        System.out.println(
                                "HTTP server started on port " + server.actualPort()
                        )
                );
    }

//    public static void main(String[] args) {
//        System.err.println(MainVerticle.class.getName());
//        io.vertx.core.Launcher.main(new String[]{"run", MainVerticle.class.getName()});
//    }
}
