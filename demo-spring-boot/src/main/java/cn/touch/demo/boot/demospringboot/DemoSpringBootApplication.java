package cn.touch.demo.boot.demospringboot;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Arrays;

@SpringBootApplication
//@SpringBootApplication(scanBasePackageClasses = {Contexts.class}, scanBasePackages = {"cn.touch.demo.boot.demospringboot.controller","cn.touch.demo.boot.demospringboot.service"})
@RestController
//@EnableTransactionManagement
//@MapperScan("cn.touch.demo.boot.demospringboot.mybatis.mapper")
//@EnableNeo4jRepositories("com.neo4j.repository")
//@EnableMongoRepositories("com.mongo.repository")
public class DemoSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootApplication.class, args);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Application Shutdown!");
		}));
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Let's inspect the beans provided by Spring Boot:");
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		};
	}

	/** 配置事务 **/
	@Bean
	@Primary
	public ChainedTransactionManager transactionManager(DataSource dataSource, SessionFactory neo4jSessionFactory, MongoDbFactory mongoDbFactory){
		/*-
			The Spring Data Neo4j Guide Book
			https://docs.spring.io/spring-data/data-neo4j/docs/3.0.x/reference/htmlsingle/#reference_programming-model_transactions
		 */
//		System.err.println(mongoDbFactory);
		return new ChainedTransactionManager(jdbcTransactionManager(dataSource), neo4jTransactionManager(neo4jSessionFactory)
//				,mongoTransactionManager(mongoDbFactory)
		);
	}

	@Bean
	@Qualifier("neo4jTransactionManager")
	public Neo4jTransactionManager neo4jTransactionManager(SessionFactory neo4jSessionFactory){
		return new Neo4jTransactionManager(neo4jSessionFactory);
	}

	@Bean
	@Qualifier("jdbcTransactionManager")
	public DataSourceTransactionManager jdbcTransactionManager(DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
	}

	/*
	Mongodb4+支持事务  MongoDB 4.0 Transaction support   , What’s New in Spring Data MongoDB 2.1

	Spring Data MongoDB  2.1.11.RELEASE,需要Spring Framework 5.1.1.RELEASE and above.
	SimpleMongoDbFactory引用到ClientSession，版本不兼容报
	java.lang.ClassNotFoundException: com.mongodb.client.ClientSession,

	Spring Data MongoDB  2.0.11.RELEASE ,旧版本是com.mongodb.session..ClientSession

	ClientSessionOptions sessionOptions = ClientSessionOptions.builder()
			.causallyConsistent(true)
			.build();
	ClientSession session = mongoClient.startSession(sessionOptions);

    public boolean transfer(String from, String to, Double money) {
        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();
            super.getDao().updateOne(clientSession, Criteria.where("name").is(to).getCriteriaObject(), inc("balance", money));
            int a = 1 / 0;
            super.getDao().updateOne(clientSession, Criteria.where("name").is(from).getCriteriaObject(), inc("balance", -money));

            clientSession.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            clientSession.abortTransaction();
            return false;
        } finally {
            clientSession.close();
        }
    }

	*/

//	@Bean
//	public MongoClient mongoClient(@Value("spring.data.mongodb.uri") String uri) {
//		MongoClient client =  new MongoClient(uri);
//		return client;
//	}

//	@Bean
//	public MongoClientFactoryBean mongoClient(@Value("spring.data.mongodb.uri") String uri) {
//		MongoClientFactoryBean mongo = new MongoClientFactoryBean();
//		mongo.setHost("127.0.0.1");
//		mongo.setPort(27017);
//		MongoCredential mongoCredential = MongoCredential.createScramSha1Credential("root","admin","root".toCharArray());
//		mongo.setCredentials(new MongoCredential[]{mongoCredential});
//		return mongo;
//	}
//
//	@Bean
//	public MongoDbFactory mongoDbFactory(MongoClient mongoClient) {
//		return new SimpleMongoDbFactory(mongoClient,"admin");
//	}
//
//	@Bean
//	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
//		return new MongoTemplate(mongoDbFactory);
//	}

//	@Bean
//	@Qualifier("mongoTransactionManager")
//	public MongoTransactionManager mongoTransactionManager(MongoDbFactory mongoDbFactory) {
//		return new MongoTransactionManager(mongoDbFactory);
//	}


}
