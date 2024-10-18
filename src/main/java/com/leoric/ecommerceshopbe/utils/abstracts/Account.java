package com.leoric.ecommerceshopbe.utils.abstracts;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Account {

    private Long id;
    private String email;
    private String mobile;

    public abstract String getName();

    public abstract String getRole();

    public abstract void setSignedOut(boolean b);
}

