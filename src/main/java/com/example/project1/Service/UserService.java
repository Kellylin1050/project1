package com.example.project1.Service;

import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService  {
    User updateUser(User Entity);
    Optional<User> findById(Integer id);
    User insertUser(User Entity);
    String deleteById(Integer id);
    User findUser(String name);
    User getUserByNameAndPassword(String name, String password) throws UsernameNotFoundException;
    User resetPassword(User password);
    User register(UserRegisterRequest userRegisterRequest);
    User login(UserLoginRequest userLoginRequest);
    //List<User> findByEmail(String email);
    //Map<String, String> generateToken(User user);

}
