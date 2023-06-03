package com.registrationlogindemo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;

// You want to extend your User class here
public class CustomUserDetails extends User implements UserDetails {
    private static final long serialVersionUID = 1L;
    private User user;

    public CustomUserDetails(User user) {
        super(user);
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You don't talk about UserRoles, so return ADMIN for everybody or implement roles.
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    public String getName() {
        return this.user.getName();
    }

    public BigDecimal getBalance() {
        return this.user.getBalance();
    }



}