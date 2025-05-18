package com.ub.ib.security.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CustomUrlFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getRawPath(); // Use getRawPath to preserve encoded chars

        // Allow encoded slashes (%2F)
        if (path.contains("%2F")) {
            // Optionally log or monitor here
        }

        // Continue the filter chain
        return chain.filter(exchange);
    }
}
