package com.example.project1.Service.impl;

import com.example.project1.Entity.MyUserDetails;
import com.example.project1.Dao.RoleRepository;
import com.example.project1.Dao.UserRepository;
//import com.example.project1.Entity.Privilege;
import com.example.project1.Entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (user == null) {
            logger.info("登入用戶: {} 不存在." , username);
            throw new UsernameNotFoundException("登入用戶: " + username + " 不存在");
        }
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        // Log user and roles for debugging
        logger.info("User: {}", user.getUsername());
        logger.info("Roles: {}", authorities.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.joining(", ")));

        return new MyUserDetails(user);
    }
}
