package cn.touch.demo.boot.demospringboot.neo4j;

import org.neo4j.driver.internal.InternalStatementResult;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.neo4j.driver.v1.*;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by chengqiang.han on 2018/11/5.
 */
public class DemoNativeNeo4j {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456"));

        try (Session session = driver.session()) {
//            createNode(session,"aaa");
//            createNode(session,"bbb");
//            queryNode(session, "aaa");

//            createRelationShip(session);

            createUnionMatch(session);


//            deleteSingleNode(session);
//            deleteRelationshipOnly(session);


//            mergeNodeAndRelationshop(session);
//            detachDeleteNode(session);
        } finally {
            driver.close();
        }
    }

    private static void deleteSingleNode(Session session) {
        InternalStatementResult result = session.writeTransaction(tx -> {
            InternalStatementResult r = (InternalStatementResult) tx.run(" MATCH (a) WHERE a.position IS NOT NULL DELETE a ");
            return r;
        });
        while (result.hasNext()) {
            System.out.println();
        }
        System.out.println("delete relationship done!");
    }

    private static void detachDeleteNode(Session session) {
        InternalStatementResult result = session.writeTransaction(tx -> {
            InternalStatementResult r = (InternalStatementResult) tx.run(" MATCH (a:Greeting)-[r:HATE_TO]->(b:Greeting) DETACH DELETE a ");
            return r;
        });
        while (result.hasNext()) {
            System.out.println();
        }
        System.out.println("delete relationship done!");
    }

    private static void deleteRelationshipOnly(Session session) {
        InternalStatementResult result = session.writeTransaction(tx -> {
//            InternalStatementResult r = (InternalStatementResult) tx.run(" MATCH ()-[r:HATE_TO_LOVE]->() DELETE r ");
            InternalStatementResult r = (InternalStatementResult) tx.run(" MATCH ()-[r:HATE_TO]->() DELETE r ");
            return r;
        });
        while (result.hasNext()) {
            System.out.println();
        }
        System.out.println("delete relationship done!");
    }


    private static void queryNode(Session session, String message) {
        InternalStatementResult result = session.writeTransaction(tx -> {
            InternalStatementResult r = (InternalStatementResult) tx.run(" MATCH (n:Greeting{message:$msg}) return n ",parameters("msg", message));
            return r;
        });
        while (result.hasNext()) {
            Record next = result.next();
            System.out.println(next.getClass()); //class org.neo4j.driver.internal.InternalRecord

            NodeValue node = (NodeValue) next.get(0); //class org.neo4j.driver.internal.value.NodeValue
            System.out.println(node);
            System.err.println(node.asNode().labels());
            System.out.println(node.get("message"));
            node.asMap(value -> {
                System.out.println(value);
                return null;
            });
        }
    }

    private static void createUnionMatch(Session session) {
        InternalStatementResult result = session.writeTransaction(tx -> {
//            InternalStatementResult r = (InternalStatementResult) tx.run("MATCH (a:Greeting{message:'aaa'})  CREATE UNIQUE (a)-[r:HATE_TO]->(b:Greeting{message:'bbb'}) return r ");
            InternalStatementResult r = (InternalStatementResult) tx.run("MATCH (a:Greeting{message:'aaa'})  CREATE UNIQUE (a)-[r:HATE_TO]->(b:Greeting{message:'hhh'}) return r ");
            return r;
        });
        while (result.hasNext()) {
            Record next = result.next();
            RelationshipValue relationship = (RelationshipValue) next.get(0);
            System.out.println(relationship.asRelationship().type());
            relationship.asMap(value -> {
                System.out.print("value: ");
                System.out.println(value);
                return null;
            });

        }
    }

    private static void createRelationShip(Session session) {
        InternalStatementResult result = session.writeTransaction(tx -> {
            InternalStatementResult r = (InternalStatementResult) tx.run("MATCH (a:Greeting{message:'aaa'}), (b:Greeting{message:'bbb'})" +
                    " CREATE (a)-[r:HATE_TO]->(b) return r ");
//                    " MERGE (a)-[r:HATE_TO]->(b) return r ");
            return r;
        });
        while (result.hasNext()) {
            Record next = result.next();
            System.out.println(next.getClass());//class org.neo4j.driver.internal.InternalRecord
            System.out.println(next.get(0).getClass());//class org.neo4j.driver.internal.value.RelationshipValue
            RelationshipValue relationship = (RelationshipValue) next.get(0);
            System.out.println(relationship.asRelationship().type());
            relationship.asMap(value -> {
                System.out.print("value: ");
                System.out.println(value);
                return null;
            });

        }
    }

    private static void mergeNodeAndRelationshop(Session session) {
        InternalStatementResult result = session.writeTransaction(tx -> {
//            InternalStatementResult r = (InternalStatementResult) tx.run(" MERGE (a:Greeting{message:'aaa'})-[r:HATE_TO]->(b:Greeting{message:'bbb'}) return r ");
            InternalStatementResult r = (InternalStatementResult) tx.run(" MATCH (b:Greeting{message:'bbb'}) MERGE (a:Greeting{message:'aaa'})-[r:HATE_TO]->(b) return r ");
            return r;
        });
        while (result.hasNext()) {
            Record next = result.next();
            RelationshipValue relationship = (RelationshipValue) next.get(0);
            relationship.asMap(value -> {
                System.out.print("value: ");
                System.out.println(value);
                return null;
            });

        }
    }

    private static void createNode(Session session, String message) {
        String greeting = session.writeTransaction(new TransactionWork<String>() {
            @Override
            public String execute(Transaction tx) {
                StatementResult result = tx.run( "CREATE (a:Greeting) " +
                                "SET a.message = $message " +
                                "RETURN a.message + ', from node ' + id(a)",
                        parameters( "message", message ) );

//                StatementResult result = tx.run( "CREATE (a:Test{name:'hcq',position:'hangzhou'}) " +
//                        "RETURN a.name + ', '+ a.position + ', from node '+ id(a)");

//                StatementResult result = tx.run( "CREATE (a{name:'hcq',position:'hangzhou'}) " +
//                        "RETURN a.name  + ', from node '+ id(a)");

                return result.single().get( 0 ).asString();
            }
        });
        System.out.println(greeting);
    }
}
