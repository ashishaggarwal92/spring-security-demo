package com.ub.ib.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UrlEncodedSlashWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String rawPath = request.getURI().getRawPath();

        if (rawPath.contains("%2F") || rawPath.contains("%2f")) {
            log.debug("Allowing request with encoded slash: {}", rawPath);
        }

        return chain.filter(exchange);
    }
}
