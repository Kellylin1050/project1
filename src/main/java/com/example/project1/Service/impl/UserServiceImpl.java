package com.example.project1.Service.impl;


import com.example.project1.Dao.UserDao;
import com.example.project1.Repository.UserRepository;
import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    @Autowired
    public UserServiceImpl(@Qualifier("userDao") UserDao userDao) {
        this.userDao = userDao;
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtGeneratorService jwtGeneratorService;




    @Override
    public Integer updateUser(User entity) {
        User updatedUser = userRepository.save(entity);
        return updatedUser.getId();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public Integer saveUser(User entity) {
        User savedUser = userRepository.save(entity);
        return savedUser.getId();
    }

    @Override
    public Integer deleteById(Integer id) {
        userRepository.deleteUserById(id);
        return id;
    }

    @Override
    public User getUserByNameAndPassword(String username, String password) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return user;
    }

    @Override
    public Map<String, String> generateToken(User user) {
        return jwtGeneratorService.generateToken(user);
    }

    @Override
    public List<User> findByEmail(String email) {
        List<User> userList = (List<User>) userDao.getUserByEmail(email);
        return userList;
    }

    @Override
    public User resetPassword(String password) {
        List<User> userList = (List<User>) userDao.resetPassword();
        return (User) userList;
    }


    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        // 檢查 user 是否存在
        if (user == null) {
            logger.info("該 email {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        // 比較密碼
        if (user.getPassword().equals(hashedPassword)) {
            return user;
        } else {
            logger.info("email {} 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }



    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setUsername(userRegisterRequest.getName());
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setEmail(userRegisterRequest.getEmail());
        user.setRoles(Collections.singleton("ROLE_USER"));
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Override
    public String findByUsername(String name) {
        userRepository.getUserByUsername(name);
        return name;
    }





    /*

    @Autowired
    private static UserDao userDao;
    @Autowired
    private UserJwtRepository userJwtRepository;

    @Autowired
    public UserServiceImpl(UserJwtRepository userJwtRepository) {
        this.userJwtRepository = userJwtRepository;
    }


    @Override
    public Integer updateUser(User entity) {
        return userDao.updateUser(entity);
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public Integer saveUser(User entity) {
        return userDao.insertUser(entity);
    }

    @Override
    public Integer deleteById(Integer id) {
        int row = userDao.deleteById(id);
        return row;
    }

    @Override
    public User findByUserName() {
       User userList = userDao.findByUserName();
        return userList;

    }

    @Override
    public User getUserByNameAndPassword(String name, String password) throws UsernameNotFoundException {
        User user = UserJwtRepository.findByUserNameAndPassword(name, password);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid id and password");
        }
        return user;
    }

    @Override
    public Map<String, String> generateToken(User user) {
        return null;
    }

    @Override
    public List<User> findByEmail(String email) {
        List<User> userList = (List<User>) userDao.getUserByEmail(email);
        return userList;
    }

    @Override
    public User resetPassword(String password) {
        List<User> userList = (List<User>) userDao.resetPassword();
        return (User) userList;
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的 email
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            logger.info("該 email {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        //創建帳號
        return userDao.CreateUser(userRegisterRequest);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        // 檢查 user 是否存在
        if (user == null) {
            logger.info("該 email {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        // 比較密碼
        if (user.getPassword().equals(hashedPassword)) {
            return user;
        } else {
            logger.info("email {} 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    @Autowired
    private UserRepository userRepository;

    public User Save(User user){
        return userRepository.save(user);
    }

    public Optional<User> findByid(Long id) {
        return userRepository.findById(id);
    }
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }*/

}




