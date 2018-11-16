package cn.touch.demo.boot.demospringboot.mongo;

//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by chengqiang.han on 2018/10/29.
 *
 *
 * 可以不用注解 @Repository,自动实例化,
 * 如果需要定制自己的个性化接口，可以在实现类中extends SimpleMongoRepository
 *
 * 也可以直接在接口中@Query
 */
public interface DemoMongoRepository extends MongoRepository<MongoPerson,String> {


    /**
     * https://docs.spring.io/spring-data/mongodb/docs/2.0.11.RELEASE/reference/html/#mongodb-template-update
     */
//    List<MongoPerson> findByLastname(String lastname);

    //extends PagingAndSortingRepository<MongoPerson, String>
//    Page<MongoPerson> findByFirstname(String firstname, Pageable pageable);
//    @Query(value="{ 'firstname' : ?0 }", fields="{ 'firstname' : 1, 'lastname' : 1}")
//    List<MongoPerson> findByThePersonsFirstname(String firstname);
//    Optional<MongoPerson> findByLoginName(String primaryKey);
}
