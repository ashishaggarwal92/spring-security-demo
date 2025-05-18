package com.ub.ib.security.filter;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class LdapUserDetails implements UserDetails {

    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private Collection<? extends GrantedAuthority> authorities;


    @Override
    public String getPassword() {
        return null;
    }
}