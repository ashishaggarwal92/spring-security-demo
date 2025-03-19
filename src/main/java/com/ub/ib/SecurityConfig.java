package com.ub.ib;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/login", "/error", "/favicon.ico").permitAll() // Allow access to login and error pages
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Optional: Convert claims to authorities
                        ));
                /*.oauth2Login(oauth2 -> oauth2 // New OAuth2 login configuration
                        .loginPage("/login") // Optionally specify the login page URL
                );*/

        return http.build();

    }

    // Configure the JwtDecoder that will decode and validate JWT tokens
    @Bean
    public JwtDecoder jwtDecoder() {
        // You can use an issuer URI or provide a custom JWKS URL if required
        return JwtDecoders.fromIssuerLocation("https://login.microsoftonline.com/{tenant-id}/v2.0");
    }


    // Configure the JwtAuthenticationConverter to extract authorities from JWT claims
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Configure the authority claim if it's in a specific claim like "roles"
        authoritiesConverter.setAuthoritiesClaimName("roles");  // Adjust this based on your JWT's claim name
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        // Set the converter on JwtAuthenticationConverter
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}