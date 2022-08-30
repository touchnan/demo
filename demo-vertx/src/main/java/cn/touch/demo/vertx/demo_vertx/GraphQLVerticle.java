package cn.touch.demo.vertx.demo_vertx;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandlerOptions;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;

import java.util.concurrent.CompletableFuture;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/8/30.
 */
public class GraphQLVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        router.route("/test").handler(BodyHandler.create().setBodyLimit(1000)).handler(event -> event.end("what"));

        GraphQL graphQL = setupGraphQLJava();
        GraphQLHandlerOptions options = new GraphQLHandlerOptions()
                .setRequestBatchingEnabled(true);//Query batching
//        router.route("/graphql").handler(GraphQLHandler.create(graphQL));

        router.post("/graphql").handler(GraphQLHandler.create(graphQL,options));

        GraphiQLHandlerOptions iqlOptions = new GraphiQLHandlerOptions()
                .setEnabled(true);

//        graphiQLHandler.graphiQLRequestHeaders(rc -> {
//            String token = rc.get("token");
//            return MultiMap.caseInsensitiveMultiMap().add("Authorization", "Bearer " + token);
//        });
        router.route("/graphiql/*").handler(GraphiQLHandler.create(iqlOptions));

        vertx.createHttpServer()
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(8080)
                // Print the port
                .onSuccess(server ->
                        System.out.println(
                                "HTTP server started on port " + server.actualPort()
                        )
                );
    }

    private GraphQL setupGraphQLJava() {
//        return GraphQL.newGraphQL(new GraphQLSchema().getType(""));
        String schema = "" +
                "type Query {\n" +
                "  bookById(id: ID): Book\n" +
                "}\n" +
                "\n" +
                "type Book {\n" +
                "  id: ID\n" +
                "  name: String\n" +
                "  pageCount: Int\n" +
                "  author: Author\n" +
                "}\n" +
                "\n" +
                "type Author {\n" +
                "  id: ID\n" +
                "  firstName: String\n" +
                "  lastName: String\n" +
                "}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("bookById", builder -> builder.dataFetcher("Book", new StaticDataFetcher("world")))
                .type("bookById", builder -> builder.dataFetcher("Author", new StaticDataFetcher("world")))
                .build();

//        DataFetcher<CompletionStage<List<Link>>> dataFetcher = environment -> {
//
//            CompletableFuture<List<Link>> completableFuture = new CompletableFuture<>();
//
//            retrieveLinksFromBackend(environment, ar -> {
//                if (ar.succeeded()) {
//                    completableFuture.complete(ar.result());
//                } else {
//                    completableFuture.completeExceptionally(ar.cause());
//                }
//            });
//
//            return completableFuture;
//        };
//
//        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
//                .type("Query", builder -> builder.dataFetcher("allLinks", dataFetcher))
//                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        return build;
    }

    public static void main(String[] args) {
//        io.vertx.core.Launcher.main(new String[]{"run", GraphQLVerticle.class.getName()});

//        runGraphQlExample();

        GraphQL graphQL = new GraphQLVerticle().setupGraphQLJava();
        ExecutionResult executionResult = graphQL.execute("{bookById{Book{name,author{firstName}}}");
        System.out.println(executionResult.getData().toString());
    }

    private static void runGraphQlExample() {
//        String schema = "type Query{hello: String} schema{query: Query}";
        String schema = "type Query{hello: String}";
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute("{hello}");

        System.out.println(executionResult.getData().toString());

    }
}
