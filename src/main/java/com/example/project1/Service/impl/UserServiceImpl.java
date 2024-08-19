package com.example.project1.Service.impl;

import com.example.project1.Dao.RoleRepository;
//import com.example.project1.Dao.UserJwtRepository;
import com.example.project1.Dao.UserRepository;
import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Dto.UserResetPasswordRequest;
import com.example.project1.Dto.UserUpdateRequest;
import com.example.project1.Entity.Role;
import com.example.project1.Entity.User;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtGeneratorService jwtGeneratorService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository =userRepository;
    }


    @Override
    public int updateUser(UserUpdateRequest userUpdateRequest) {
        return userRepository.updateUser(userUpdateRequest.getId(), userUpdateRequest.getName(), userUpdateRequest.getUsername(), userUpdateRequest.getPhone());
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User insertUser(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    @Override
    public User findUser(String username) {
        return userRepository.getUserByUsername(username);

    }

    @Override
    public int resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
        String hashedPassword = DigestUtils.md5DigestAsHex(userResetPasswordRequest.getPassword().getBytes());
        userResetPasswordRequest.setPassword(hashedPassword);
        return userRepository.resetPassword(userResetPasswordRequest.getId(),hashedPassword);

    }
    @Override
    public User register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的 email
        User user = userRepository.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            logger.info("該 email {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);
        //創建帳號
        User user1 = new User();
        user1.setName(userRegisterRequest.getName());
        user1.setPassword(userRegisterRequest.getPassword());
        user1.setEmail(userRegisterRequest.getEmail());
        user1.setUsername(userRegisterRequest.getUsername());
        Role userRole = roleRepository.findByName("ROLE_USER");
        user1.setRoles(Collections.singleton(userRole));
        user1.setEnabled(true);
        return userRepository.save(user1);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userRepository.getUserByUsername(userLoginRequest.getUsername());
        return user;
    }
}


