package com.example.project1.security;

import com.example.project1.Dao.RoleRepository;
import com.example.project1.Dao.UserRepository;
import com.example.project1.Entity.Role;
import com.example.project1.Entity.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer {
    @Autowired
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {

        // 如果 "ROLE_ADMIN" 不存在，則創建它
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        // 如果 "ROLE_USER" 不存在，則創建它
        if (roleRepository.findByName("ROLE_USER") == null) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        if (userRepository.getUserByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setName("admin");
            String rawPassword = "admin";
            String hashedPassword = DigestUtils.md5DigestAsHex(rawPassword.getBytes());
            admin.setPassword(hashedPassword);
            admin.setEmail("admin@gmail.com");
            admin.setEnabled(true);
            Role userRole = roleRepository.findByName("ROLE_ADMIN");
            admin.setRoles(Collections.singleton(userRole));
            userRepository.save(admin);
        }
    }
}
