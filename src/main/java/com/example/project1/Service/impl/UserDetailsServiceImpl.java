package com.example.project1.Service.impl;

import com.example.project1.Dao.MyUserDetails;
import com.example.project1.Dao.RoleRepository;
import com.example.project1.Dao.UserRepository;
//import com.example.project1.Entity.Privilege;
import com.example.project1.Entity.Role;
import com.example.project1.Entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);

       // List<SimpleGrantedAuthority> authorities = new ArrayList<>();
       // authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
       // List<String> roles = (List<String>) roleRepository.findByName(username);
       // System.out.println(roles);

        if (user == null) {
            logger.info("登入用戶: {} 不存在." , username);
            throw new UsernameNotFoundException("登入用戶: " + username + " 不存在");
        }
        /*List<String> roles =roleRepository.getRoleByUserName(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role :roles){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }*/
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        // Log user and roles for debugging
        logger.info("User: {}", user.getUsername());
        logger.info("Roles: {}", authorities.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")));

        return new MyUserDetails(user);

        // Debugging
        //System.out.println("User: " + user.getUsername());
        //System.out.println("Roles: " + user.getRoles());


    }

    /*@Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Arrays.asList(
                            roleRepository.findByName("ROLE_USER"))));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
                true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }*/
}
