package com.ub.ib.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CustomUrlFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("CustomUrlFilter Order: 2 ");
        String path = exchange.getRequest().getURI().getRawPath(); // Use getRawPath to preserve encoded chars

        // Allow encoded slashes (%2F)
        if (path.contains("%2F")) {
            // Optionally log or monitor here
            // Do not filter
            log.info("Path contains encoded slash");
        }

        // Continue the filter chain
        return chain.filter(exchange);
    }
}
