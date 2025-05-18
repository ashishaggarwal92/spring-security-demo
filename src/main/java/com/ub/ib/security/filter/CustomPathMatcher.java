package com.ub.ib.security.filter;

import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec;
import org.springframework.stereotype.Component;

@Component
public class CustomPathMatcher {

    public void configureMatchers(AuthorizeExchangeSpec authorize) {
        authorize
                .pathMatchers("/login", "/error", "/favicon.ico").permitAll()
                .anyExchange().access(new HasAnyAuthorityAuthorizationManager());
    }
}
