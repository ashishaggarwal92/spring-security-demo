package com.ub.ib.security.filter;

import com.ub.ib.security.filter.CorsConfig;
import com.ub.ib.security.filter.CustomPathMatcher;
import com.ub.ib.security.filter.CustomUrlFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebFluxSecurityConfig {

    private final CustomUrlFilter customUrlFilter;
    private final CustomPathMatcher customPathMatcher;
    private final CorsConfig corsConfig;
    LdapService ldapService;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(
                        auth -> customPathMatcher.configureMatchers(auth)
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter()) // Optional: Convert claims to authorities
                        ))
        ;
        http.addFilterBefore(customUrlFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();

    }


    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> customJwtAuthenticationConverter() {
        return jwt -> {
            String username = jwt.getClaimAsString("preferred_username");

            Mono<LdapUserDetails> userDetailsMono = ldapService.loadUserByUsername(username);
            Mono<Collection<GrantedAuthority>> authoritiesMono = ldapService.getAuthoritiesByUsername(username);

            return Mono.zip(userDetailsMono, authoritiesMono)
                    .map(tuple -> {
                        LdapUserDetails userDetails = tuple.getT1();
                        Collection<GrantedAuthority> authorities = tuple.getT2();

                        JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
                        authentication.setDetails(userDetails); // Add extra info to the security context

                        return authentication;
                    });
        };
    }


}