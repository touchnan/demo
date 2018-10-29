package cn.touch.demo.boot.demospringboot.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by chengqiang.han on 2018/10/29.
 */
@Service
public class MongoService {

/** autoConfiguration, 自动加载实例化的类：
 *
 * mongo:class com.mongodb.MongoClient
 * mongoAccountService:class cn.coresnow.unrealcity.serviceaccount.service.MongoAccountService
 * mongoCustomConversions:class org.springframework.data.mongodb.core.convert.MongoCustomConversions
 * mongoDbFactory:class org.springframework.data.mongodb.core.SimpleMongoDbFactory
 * mongoMappingContext:class org.springframework.data.mongodb.core.mapping.MongoMappingContext
 * mongoTemplate:class org.springframework.data.mongodb.core.MongoTemplate
 */



    /**************************************  使用Spring Data Repository  *************************************/
    @Autowired
    private DemoMongoRepository demoMongoRepository;

    public void repository() {
        MongoPerson p = new MongoPerson();
        demoMongoRepository.save(p);
        Optional<MongoPerson> one = demoMongoRepository.findOne(Example.of(p));
        one.ifPresent(person -> {
        });
        List<MongoPerson> persons = demoMongoRepository.findAll();
    }

    /**************************************  不使用Spring Data Repository  *************************************/

    @Autowired
    private MongoClient mongo;
    @Autowired
    private MongoDbFactory mongoDbFactory;
    @Autowired
    private MongoOperations mongoTemplate;//mongoOps

    public void noRepository() {
        mongoTemplate.find(Query.query(Criteria.where("id").is("")),null);
        /**
         MongoPerson p = new MongoPerson("Joe", 34);

         // Insert is used to initially store the object into the database.
         mongoOps.insert(p);
         log.info("Insert: " + p);

         // Find
         p = mongoOps.findById(p.getId(), MongoPerson.class);
         log.info("Found: " + p);

         // Update
         mongoOps.updateFirst(query(where("name").is("Joe")), update("age", 35), MongoPerson.class);
         p = mongoOps.findOne(query(where("name").is("Joe")), MongoPerson.class);
         log.info("Updated: " + p);

         // Delete
         mongoOps.remove(p);

         // Check that deletion worked
         List<MongoPerson> people =  mongoOps.findAll(MongoPerson.class);
         log.info("Number of people = : " + people.size());


         mongoOps.dropCollection(MongoPerson.class);
         */

        MongoIterable<String> strings = mongo.listDatabaseNames();
        for (String name : strings) {
            System.out.println(name);
        }
        MongoDatabase db = mongo.getDatabase("hcq");
//        db.drop();
//        db.createCollection("MongoPerson");
        MongoCollection<Document> collection = db.getCollection("MongoPerson");
        FindIterable<Document> documents = collection.find(Filters.eq("_id", new ObjectId("")));
        for (Document doc : documents) {
            System.out.println(doc.getBoolean(""));
        }
    }
}
