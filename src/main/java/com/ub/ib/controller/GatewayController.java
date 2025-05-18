package com.ub.ib.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @GetMapping("/test")
    public String test() {
        return "Gateway is working!";
    }

    @GetMapping("/test1")
    public String test(@AuthenticationPrincipal OAuth2User principal) {
        return "Hello " + (principal != null ? principal.getAttribute("name") : "Guest");
    }
}