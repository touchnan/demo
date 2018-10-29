package cn.touch.demo.boot.demospringboot.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by chengqiang.han on 2018/10/29.
 *
 *
 * 可以不用注解 @Repository,自动实例化,
 * 如果需要定制自己的个性化接口，可以在实现类中extends SimpleMongoRepository
 */
public interface DemoMongoRepository extends MongoRepository<MongoPerson,String> {
}
