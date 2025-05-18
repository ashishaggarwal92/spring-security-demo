package com.ub.ib.security.filter;

import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface LdapService {

    Mono<LdapUserDetails> loadUserByUsername(String username);
    Mono<Collection<GrantedAuthority>> getAuthoritiesByUsername(String userName);
}
