package com.ub.ib.security.filter;


import com.ub.ib.security.domain.AuthDetailSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
@Order(101)
@Slf4j// Ensures this filter runs after authentication
public class RequestHeaderAppenderFilterAfterAuthentication implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("RequestHeaderAppenderFilterAfterAuthentication ");
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth-> {
                    Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
                    Jwt authPrincipal = (Jwt) auth.getPrincipal();
                    AuthDetailSource authDetailSource = (AuthDetailSource) auth.getDetails();


                    return chain.filter(exchange);
                });

    }
}
