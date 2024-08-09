package com.example.project1.Service;

import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Dto.UserResetPasswordRequest;
import com.example.project1.Dto.UserUpdateRequest;
import com.example.project1.Entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public interface UserService  {
    int updateUser(UserUpdateRequest userUpdateRequest);
    Optional<User> findById(Integer id);
    User insertUser(User Entity);
    void deleteById(Integer id);
    User findUser(String name);
    User getUserByNameAndPassword(String name, String password) throws UsernameNotFoundException;
    int resetPassword(UserResetPasswordRequest userResetPasswordRequest);
    User register(UserRegisterRequest userRegisterRequest);
    User login(UserLoginRequest userLoginRequest);

    boolean existsById(Integer id);
    //List<User> findByEmail(String email);
    //Map<String, String> generateToken(User user);

}
