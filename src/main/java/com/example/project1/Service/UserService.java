package com.example.project1.Service;

import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface UserService  {
    Integer updateUser(User Entity);
    Optional<User> findById(Long id);
    Integer saveUser(User Entity);
    Integer deleteById(Integer id);

    public User getUserByNameAndPassword(String name, String password) throws UsernameNotFoundException;

    Map<String, String> generateToken(User user);

    List<User> findByEmail(String email);
    User resetPassword(String password);

    User login(UserLoginRequest userLoginRequest);

    Integer register(UserRegisterRequest userRegisterRequest);

    String findByUsername(String name);
}
    /*
        Integer updateUser(User entity);
        User findById(Long id);
        Integer saveUser(User entity);

        Long deleteById(Long id);

        User getUserByNameAndPassword(String name, String password) throws UsernameNotFoundException;
        Map<String, String> generateToken(User user);
        List<User> findByEmail(String email);
        User resetPassword(String email);
        User login(UserLoginRequest userLoginRequest);
        Integer register(UserRegisterRequest userRegisterRequest);


        String findByUsername(String username);/*
}*/
