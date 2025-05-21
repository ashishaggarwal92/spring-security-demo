package com.ub.ib.controller;

import com.ub.ib.security.domain.AuthDetailSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class GatewayController {

    @GetMapping("/test")
    public String test() {
        return "Gateway is working!";
    }

    @GetMapping("/test1")
    public String test(Authentication authentication) {


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        AuthDetailSource authDetailSource = (AuthDetailSource) authentication.getDetails();

        return authDetailSource.toString();
    }
}