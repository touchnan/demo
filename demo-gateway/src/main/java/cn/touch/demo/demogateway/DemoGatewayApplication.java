package cn.touch.demo.demogateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

import static org.springframework.cloud.gateway.support.RouteMetadataUtils.CONNECT_TIMEOUT_ATTR;
import static org.springframework.cloud.gateway.support.RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR;

@SpringBootApplication
public class DemoGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoGatewayApplication.class, args);

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
				Object bean = ctx.getBean(beanName);
				if (true || beanName.indexOf("neo4j") != -1 || bean.getClass().toString().indexOf("neo4j") != -1
						|| beanName.indexOf("customConverters") != -1) {
					System.err.print(beanName);
					System.err.print(":");
					System.err.println(bean.getClass());
				}
			}
		};
	}

//	@Bean
//	public RequestContextListener requestContextListener(){
//		return new RequestContextListener();
//	}

//	@Bean
//	AllFilter filter() {
//		return new AllFilter();
//	}


	@Bean
	public RouteLocator allRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/gongan/**")
						.filters(f -> f.filter((exchange, chain) -> chain.filter(exchange), 0))
						.uri("http://localhost:88")
//						.order(0)
//						.id("all_filter_router")
				)

				.route("direct-route",r -> r.remoteAddr("10.1.1.1", "10.10.1.1/24").uri("https://downstream1"))
//				.route("proxied-route",r -> r.remoteAddr(resolver, "10.10.1.1", "10.10.1.1/24").uri("https://downstream2"))

//				.route("circuitbreaker_route", r -> r.path("/consumingServiceEndpoint")
//						.filters(f -> f.circuitBreaker(c -> c.name("myCircuitBreaker").fallbackUri("forward:/inCaseOfFailureUseThis").addStatusCode("INTERNAL_SERVER_ERROR"))
//								.rewritePath("/consumingServiceEndpoint", "/backingServiceEndpoint")).uri("lb://backing-service:8088"))
				.route("test1", r -> {
					return r.host("*.somehost.org").and().path("/somepath")
							.filters(f -> f.addRequestHeader("header1", "header-value-1"))
							.metadata(RESPONSE_TIMEOUT_ATTR, 200)
							.metadata(CONNECT_TIMEOUT_ATTR, 200)
							.uri("http://someuri");
				})
//				.route(r -> r.path(routesIndexPath)
////						.filters(f -> f.filter(new ApiFilter()))
//						.uri(routesIndexUrl)
//						.order(1)
//						.id("all_filter_router"))
//				.route(r -> r.path(routesIndexPath)
////						.filters(f -> f.filter(new ApiFilter()))
//						.uri(routesIndexUrl)
//						.order(1)
//						.id("all_filter_router"))
				.build();
	}


}
