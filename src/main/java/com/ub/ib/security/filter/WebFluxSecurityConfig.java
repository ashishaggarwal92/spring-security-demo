package com.ub.ib.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebFluxSecurityConfig {

    private final CustomUrlFilter customUrlFilter;
    private final CustomPathMatcher customPathMatcher;
    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(
                        auth -> customPathMatcher.configureMatchers(auth)
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter) // Optional: Convert claims to authorities
                        ))
        ;
        http.addFilterBefore(customUrlFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();

    }




}