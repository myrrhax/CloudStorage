package com.myrr.CloudStorage.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtEntity implements UserDetails {
    private final long id;
    private final String name;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtEntity(long id,
                     String name,
                     String password,
                     Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "JwtEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
