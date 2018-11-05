package cn.touch.demo.boot.demospringboot.neo4j;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by chengqiang.han on 2018/11/5.
 */
public class Neo4jService {

    /** autoConfiguration, 自动加载实例化的类：
     *
     *      configuration:class org.neo4j.ogm.config.Configuration
     *      neo4jAuditionBeanFactoryPostProcessor:class org.springframework.data.neo4j.repository.support.Neo4jAuditingBeanFactoryPostProcessor
     *      neo4jMappingContext:class org.springframework.data.neo4j.mapping.Neo4jMappingContext
     *      neo4jOpenSessionInViewInterceptor:class org.springframework.data.neo4j.web.support.OpenSessionInViewInterceptor
     *      neo4jPersistenceExceptionTranslator:class org.springframework.data.neo4j.repository.support.Neo4jPersistenceExceptionTranslator
     *      neo4jTransactionManager:class org.springframework.data.neo4j.transaction.Neo4jTransactionManager
     *      org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration:class org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration$$EnhancerBySpringCGLIB$$cd8a1da9
     *      org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration$Neo4jWebConfiguration:class org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration$Neo4jWebConfiguration$$EnhancerBySpringCGLIB$$184b295
     *      org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration$Neo4jWebConfiguration$Neo4jWebMvcConfiguration:class org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration$Neo4jWebConfiguration$Neo4jWebMvcConfiguration$$EnhancerBySpringCGLIB$$d7f6a555
     *      org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration:class org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration$$EnhancerBySpringCGLIB$$5da1dbeb
     *      sessionFactory:class org.neo4j.ogm.session.SessionFactory
     *      spring.data.neo4j-org.springframework.boot.autoconfigure.data.neo4j.Neo4jProperties:class org.springframework.boot.autoconfigure.data.neo4j.Neo4jProperties
     */

    /**************************************  使用Spring Data Repository  *************************************/

    @Autowired
    private DemoNeo4jRepository demoNeo4jRepository;

    public void repository() {
        Optional<Neo4jMovie> byId = demoNeo4jRepository.findById(1L);
        byId.ifPresent(neo4jMovie -> {

        });

        Iterable<Neo4jMovie> all = demoNeo4jRepository.findAll();

        List<Neo4jMovie> movies = demoNeo4jRepository.getMoivesByActor("王浩");
        for (Neo4jMovie m : movies) {
            System.out.println(m.getTitle());
        }

        //关系无法返回，总为空
        Neo4jRating r  =  demoNeo4jRepository.getRateRelation("王浩", "The Good doctor");
        System.out.println("getRateRelation");
        System.out.println(r.getRating());
        System.out.println(r.getRateDate());
//        System.out.println(r.getPerson().getName());
//        System.out.println(r.getMovie().getTitle());

        Neo4jMovie m = demoNeo4jRepository.getMovie("The Good doctor");
        System.out.println("getMovie  by Name");
        System.out.println(m.getTitle());
        Neo4jPerson p = demoNeo4jRepository.createPerson("附加符号", 1345667L);
        System.out.println("createPerson");
        System.out.println(p.getName());
        System.out.println(p.getMobile());

        List<Neo4jPerson> the_good_doctor = demoNeo4jRepository.getPersonsByMovie("The Good doctor");
        for (Neo4jPerson person : the_good_doctor) {
            System.out.println(person.getName());
        }

        Optional<Neo4jMovie> mbyId = demoNeo4jRepository.findById(85L);
        mbyId.ifPresent(neo4jMovie -> {
            System.out.println(neo4jMovie.getTitle());
        });

        p  =  demoNeo4jRepository.getRatePerson("王浩", "The Good doctor");
        System.out.println("getRatePerson  by Name");
        System.out.println(p.getName());
        System.out.println(p.getMobile());

        p = demoNeo4jRepository.getPerson("刘国梁");
        System.out.println("getPerson  by Name");
        System.out.println(p.getName());
        System.out.println(p.getMobile());

        p = demoNeo4jRepository.getPerson(124L);
        System.out.println("getPerson  by mobile");
        System.out.println(p.getName());
        System.out.println(p.getMobile());
    }

    /**************************************  不使用Spring Data Repository  *************************************/

    @Autowired
    private SessionFactory sessionFactory;

    public void noRepository() {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Result query = session.query("", null);
        Iterable<Map<String, Object>> maps = query.queryResults();
//        QueryStatistics queryStatistics = query.queryStatistics();
        transaction.commit();
        transaction.rollback();


        session = sessionFactory.openSession();
        Neo4jMovie movie = new Neo4jMovie();
        movie.setTitle("The Good doctor");
        Neo4jPerson p = new Neo4jPerson();
        p.setMobile(603L);
        p.setName("刘国梁");
        movie.setDirector(p);


        Set<Neo4jPerson> actors = new HashSet<>(2);
        List<Neo4jRating> ratings = new ArrayList<>(2);
        Neo4jPerson p1 = new Neo4jPerson();
        p1.setMobile(124L);
        p1.setName("王浩");
        actors.add(p1);

        Neo4jRating rating = new Neo4jRating();
        rating.setMovie(movie);
        rating.setRateDate(new Date());
        rating.setRating(4);
        rating.setPerson(p1);
        ratings.add(rating);

        p1 = new Neo4jPerson();
        p1.setMobile(125L);
        p1.setName("马琳");
        actors.add(p1);

        rating = new Neo4jRating();
        rating.setMovie(movie);
        rating.setRateDate(new Date());
        rating.setRating(5);
        rating.setPerson(p1);
        ratings.add(rating);

        movie.setActors(actors);
        movie.setRatings(ratings);
        session.save(movie);

    }
}
