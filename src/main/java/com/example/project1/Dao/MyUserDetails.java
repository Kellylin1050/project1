package com.example.project1.Dao;

import com.example.project1.Entity.Role;
import com.example.project1.Entity.User;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {


   private User user;

    public MyUserDetails(User user){
        this.user = user;
    }

    /*public static org.springframework.security.core.userdetails.User.UserBuilder builder() {
        return null;
    }*/

    //private Collection<? extends GrantedAuthority> authorities;
    /*public MyUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
           authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        return authorities;*/
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        //return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

    /*@Override
    public String toString() {
        return "JwtUser{" +
                "username='" +  username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }*/
}
