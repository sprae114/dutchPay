package com.example.dutchpay.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LoginPrincipal implements UserDetails, OAuth2User {
    private Long id;
    private String name;
    private String email;

    Collection<? extends GrantedAuthority> authorities;
    Map<String, Object> oAuth2Attributes;

    public LoginPrincipal() {
        this.id = 0L;
        this.name = null;
        this.email = null;
    }

    public LoginPrincipal(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public LoginPrincipal(Long id, String name, String email, Collection<? extends GrantedAuthority> authorities, Map<String, Object> oAuth2Attributes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.authorities = authorities;
        this.oAuth2Attributes = oAuth2Attributes;
    }

    public static LoginPrincipal of(Long id, String name, String email) {
        return of(id, name, email, Map.of());
    }

    public static LoginPrincipal of(Long id, String name, String email, Map<String, Object> oAuth2Attributes) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new LoginPrincipal(
                id,
                name,
                email,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                oAuth2Attributes
        );
    }

    public static LoginPrincipal from(UserAccountDto dto) {
        return LoginPrincipal.of(
                dto.getId(),
                dto.getName(),
                dto.getEmail()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {return null;}

    @Override
    public String getUsername() {return name;}

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}


    public enum RoleType {
        USER("ROLE_USER");

        private final String name;

        public String getName() {
            return name;
        }

        RoleType(String name) {
            this.name = name;
        }
    }
}