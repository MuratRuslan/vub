package de.zwickau.vub.vending.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    BUYER, SELLER;


    @Override
    public String toString() {
        return this.name();
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
