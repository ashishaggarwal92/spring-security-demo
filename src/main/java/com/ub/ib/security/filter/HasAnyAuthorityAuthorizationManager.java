package com.ub.ib.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class HasAnyAuthorityAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    /*
    * Check if any granted authority exist before completing authentication

     */

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        log.info("HasAnyAuthorityAuthorizationManager");
        return authentication
                .filter(Authentication::isAuthenticated)
                .map(auth -> !auth.getAuthorities().isEmpty())
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}