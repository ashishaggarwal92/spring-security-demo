package com.ub.ib.security.domain;

import lombok.Getter;

import java.util.Map;

@Getter
public class AuthDetail {

    Map<String, String> claims;
}
