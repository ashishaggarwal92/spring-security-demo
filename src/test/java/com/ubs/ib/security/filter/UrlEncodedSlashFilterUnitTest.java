package com.ubs.ib.security.filter;

import com.ub.ib.security.filter.UrlEncodedSlashWebFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebFilter;

@WebFluxTest
public class UrlEncodedSlashFilterUnitTest {

    private final WebTestClient webTestClient;

    UrlEncodedSlashFilterUnitTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void shouldAllowEncodedSlashEverywhere() {
        webTestClient.get()
                .uri("/any/path%2Fwith%2Fencoded")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("OK");
    }

    @Configuration
    static class TestConfig {
        @Bean
        public WebFilter urlEncodedSlashFilter() {
            return new UrlEncodedSlashWebFilter();
        }

        @Bean
        public DummyController dummyController() {
            return new DummyController();
        }
    }

    @RestController
    static class DummyController {
        @GetMapping("/any/{path}")
        public String handler() {
            return "OK";
        }
    }
}
