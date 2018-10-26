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
	@Qualifier("neo4jTransactionManager")
	public Neo4jTransactionManager neo4jTransactionManager(SessionFactory neo4jSessionFactory){
		return new Neo4jTransactionManager(neo4jSessionFactory);
	}

	@Bean
	@Qualifier("jdbcTransactionManager")
	public DataSourceTransactionManager jdbcTransactionManager(DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
	}

//	@Bean
//	@Qualifier("mongoTransactionManager")
//	public MongoTransactionManager mongoTransactionManager(MongoDbFactory mongoDbFactory) {
//		return new MongoTransactionManager(mongoDbFactory);
//	}

	@Bean
	@Primary
	public ChainedTransactionManager transactionManager(DataSource dataSource, SessionFactory neo4jSessionFactory, MongoDbFactory mongoDbFactory){
		/*-
			The Spring Data Neo4j Guide Book
			https://docs.spring.io/spring-data/data-neo4j/docs/3.0.x/reference/htmlsingle/#reference_programming-model_transactions
		 */
		return new ChainedTransactionManager(jdbcTransactionManager(dataSource), neo4jTransactionManager(neo4jSessionFactory)
//				,mongoTransactionManager(mongoDbFactory)
		);
	}
}
