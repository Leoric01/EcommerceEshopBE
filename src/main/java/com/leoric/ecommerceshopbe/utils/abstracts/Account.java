package com.leoric.ecommerceshopbe.utils.abstracts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
public abstract class Account {

    private Long id;
    private String email;
    private String mobile;
    private boolean isSignedOut;
    public abstract String getName();
    public abstract String getRole();

    //this is redundant field, but it makes sure you don't return generic wild card
    //in any future types of accounts(for example moderator, admin, guest etc...)
    public abstract Collection<GrantedAuthority> getAuthorities();
}

