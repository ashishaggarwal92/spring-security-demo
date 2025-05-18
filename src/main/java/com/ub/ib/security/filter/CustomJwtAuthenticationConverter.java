package com.ub.ib.security.filter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Mono<JwtAuthenticationToken>> {

    private final LdapService ldapService;

    public CustomJwtAuthenticationConverter(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @Override
    public Mono<JwtAuthenticationToken> convert(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null || username.isEmpty()) {
            return Mono.error(new UsernameNotFoundException("JWT missing preferred_username claim"));
        }

        return ldapService.loadUserByUsername(username)
                .flatMap(userDetails ->
                        ldapService.getAuthoritiesByUserId(userDetails.getUserId())
                                .map(ldapGroups -> {
                                    // Convert LDAPGroups to GrantedAuthority
                                    List<SimpleGrantedAuthority> authorities = ldapGroups.stream()
                                            .map(group -> new SimpleGrantedAuthority(group.getGroupName())) // Adjust if different getter
                                            .toList();

                                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
                                    authentication.setDetails(userDetails);
                                    return authentication;
                                })
                );
    }

}

