package cn.touch.demo.demogateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/11/25.
 */
@Component
public class AllFilter implements GlobalFilter, Ordered {
    public static final Logger log = LoggerFactory.getLogger(AllFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> authorization = exchange.getRequest().getHeaders().get("authorization");
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();


        log.debug("request", exchange.getRequest().getPath());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
