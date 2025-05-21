package com.ub.ib.security.domain;

import lombok.Data;

import java.util.List;

@Data
public class AuthDetailSource {

    UserDetail userDetail;
    List<String> allowedAgrType;
    List<Region> allowedRegion;
    Boolean allowedConfidentialParty;


}
