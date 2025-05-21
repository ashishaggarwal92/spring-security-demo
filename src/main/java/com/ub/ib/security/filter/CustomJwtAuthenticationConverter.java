package com.ub.ib.security.filter;

import com.ub.ib.security.domain.AuthDetailSource;
import com.ub.ib.security.domain.UserDetail;
import com.ub.ib.security.ldap.LdapGroup;
import com.ub.ib.security.ldap.LdapService;
import com.ub.ib.security.ldap.LdapUserDetails;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, Mono<JwtAuthenticationToken>> {

    private final LdapService ldapService;

    public CustomJwtAuthenticationConverter(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @Override
    public Mono<JwtAuthenticationToken> convert(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null || username.isEmpty()) {
            return Mono.error(new UsernameNotFoundException("JWT missing preferred_username claim"));
        }

        return ldapService.loadUserByUsername(username)
                .flatMap(userDetails -> {

                    AuthDetailSource authDetail = new AuthDetailSource();
                    populateUserDetail(authDetail, userDetails);
                    List<SimpleGrantedAuthority> authorities = authorities(userDetails.getUserId());

                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);
                    authentication.setDetails(authDetail);
                    return Mono.just(authentication);

                });
    }

    private void populateUserDetail(AuthDetailSource authDetail, LdapUserDetails userDetails) {

        UserDetail userDetail = authDetail.getUserDetail();
        userDetail.setFirstName(userDetail.getFirstName());
    }

    private List<SimpleGrantedAuthority> authorities(String userId){
        List<LdapGroup> ldapGroups= ldapService.getAuthoritiesByUserId(userId);
        return ldapGroups.stream()
                .map(group -> new SimpleGrantedAuthority(group.getGroupName())) // Adjust if different getter
                .toList();


    }

}

