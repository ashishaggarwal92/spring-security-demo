package com.ub.ib.security.ldap;

import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

public interface LdapService {

    Mono<LdapUserDetails> loadUserByUsername(String username);
    List<LdapGroup> getAuthoritiesByUserId(String userName);
}
