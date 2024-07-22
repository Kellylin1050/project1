package com.example.project1.Service.impl;

import com.example.project1.Dao.UserJwtRepository;
import com.example.project1.Dao.UserRepository;
import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    @Autowired
    private UserJwtRepository userJwtRepository;

    @Autowired
    private JwtGeneratorService jwtGeneratorService;

    @Autowired
    public UserServiceImpl(UserJwtRepository userJwtRepository,UserRepository userRepository) {
        this.userJwtRepository = userJwtRepository;
        this.userRepository =userRepository;
    }


    @Override
    public User updateUser(User entity) {
        return userRepository.save(entity);
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
    public String deleteById(Integer id) {
        userRepository.deleteById(id);
        return "delete";
    }

    @Override
    public User findUser(String name) {
        return userRepository.getUserByUsername(name);

    }

    @Override
    public User getUserByNameAndPassword(String email, String password) throws UsernameNotFoundException {
        User user = UserJwtRepository.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid id and password");
        }
        return user;
    }

    /*@Override
    public Map<String, String> generateToken(User user) {
        return null;
    }

    @Override
    public List<User> findByEmail(String email) {
        List<User> userList = (List<User>) userRepository.getUserByEmail(email);
        return userList;
    }*/

    @Override
    public User resetPassword(User password) {
        return userRepository.save(password);

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
        return userRepository.save(user1);


    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userRepository.getUserByEmail(userLoginRequest.getEmail());
        return user;

        // 檢查 user 是否存在
        /*if (user == null) {
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
        }*/
    }
    /*@Autowired
    private UserDao.UserRepository userRepository;

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


