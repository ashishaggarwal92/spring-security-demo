package com.ub.ib;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomRouter {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("publicService", r -> r.path("/public/**")
                        .uri("lb://PUBLIC-SERVICE")  // Or use direct URL if needed
                )
                .route("service1", r -> r.path("/service1/**")
                        .uri("lb://SERVICE-1")  // Or use direct URL
                        //.filters(f -> f.addRequestHeader("X-Auth-User", "#{authentication?.name}")) // Pass the authenticated user
                )
                .route("service2", r -> r.path("/service2/**")
                        .uri("lb://SERVICE-2")  // Or use direct URL
                        //.filters(f -> f.addRequestHeader("X-Auth-User", "#{authentication?.name}")) // Pass the authenticated user
                )
                .build();
    }
}
