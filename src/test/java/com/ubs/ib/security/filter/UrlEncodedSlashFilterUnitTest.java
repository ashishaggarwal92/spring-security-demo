package com.ubs.ib.security.filter;

import com.ub.ib.security.filter.UrlEncodedSlashWebFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebFilter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebFluxTest
public class UrlEncodedSlashFilterUnitTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void shouldAllowEncodedSlashEverywhere() {
        webTestClient.get()
                .uri("/api/test%2F123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Handled: test/123");
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
        @GetMapping("/api/{id}")
        public String handle(@PathVariable String id) {
            return "Handled: " + URLDecoder.decode(id, StandardCharsets.UTF_8);
        }
    }
}
