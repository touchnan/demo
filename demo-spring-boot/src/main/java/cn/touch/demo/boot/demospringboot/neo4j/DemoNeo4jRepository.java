package cn.touch.demo.boot.demospringboot.neo4j;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by chengqiang.han on 2018/11/5.
 *
 * 可以不用注解 @Repository,自动实例化,
 * 如果需要定制自己的个性化接口，可直接定义在接口@Query，也可以在实现类中extends SimpleNeo4jRepository
 */

public interface DemoNeo4jRepository extends Neo4jRepository<Neo4jMovie,Long> {


    @Query("MATCH (p:Person)-[:ACTS_IN]->(m:Movie) WHERE p.name=$name return m")
    public List<Neo4jMovie> getMoivesByActor(@Param("name") String userName);

    @Query("MATCH (p:Person{name:$personName}) -[r:RATED]-> (m:Movie{title:$movieName}) return r")
    Neo4jRating getRateRelation(@Param("personName") String personName, @Param("movieName") String movieName);//关系无法返回，总为空

    @Query("MATCH (n:Movie{title:$title}) RETURN n")
    Neo4jMovie getMovie(@Param("title") String title);

    @Query("CREATE (n:Person{name:$name,mobile:$mobile}) RETURN n")
    Neo4jPerson createPerson(@Param("name") String name, @Param("mobile") long mobile);


    @Query("MATCH (p:Person) -[:ACTS_IN]-> (m:Movie{title:$title}) return p")
    public List<Neo4jPerson> getPersonsByMovie(@Param("title") String title);

    @Query("MATCH (p:Person{name:$personName}) -[r:ACTS_IN]-> (m:Movie{title:$movieName}) return p")
    Neo4jPerson getRatePerson(@Param("personName") String personName, @Param("movieName") String movieName);

    @Query("MATCH (n:Person{name:$name}) RETURN n")
    Neo4jPerson getPerson(@Param("name") String name);

    @Query("MATCH (n:Person{mobile:$mobile}) RETURN n")
    Neo4jPerson getPerson(@Param("mobile") long mobile);

    /**

     MATCH (n:Neo4jMovie) RETURN n LIMIT 25
     MATCH (n) WHERE EXISTS(n.name) RETURN DISTINCT "node" as entity, n.name AS name LIMIT 25 UNION ALL MATCH ()-[r]-() WHERE EXISTS(r.name) RETURN DISTINCT "relationship" AS entity, r.name AS name LIMIT 25

     */
}
