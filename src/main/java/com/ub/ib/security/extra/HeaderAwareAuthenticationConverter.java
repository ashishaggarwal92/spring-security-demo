package com.ub.ib.security.extra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class HeaderAwareAuthenticationConverter implements ServerAuthenticationConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<AbstractAuthenticationToken> convert(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }

        String token = authHeader.substring(7);
        String rolesJson = exchange.getRequest().getHeaders().getFirst("X-User-Roles");

        Set<String> roles = parseRoles(rolesJson);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // For demonstration only: use a dummy Jwt — in real case use a JwtDecoder
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("sub", "user-from-token")
                .build();

        JwtAuthenticationToken jwtAuthentication = new JwtAuthenticationToken(jwt, authorities);
        return Mono.just(jwtAuthentication);
    }

    private Set<String> parseRoles(String rolesJson) {
        if (rolesJson == null || rolesJson.isBlank()) return Set.of();
        try {
            List<String> list = objectMapper.readValue(
                    rolesJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
            );
            return new HashSet<>(list);
        } catch (Exception e) {
            throw new RuntimeException("Invalid X-User-Roles header JSON: " + rolesJson, e);
        }
    }
}