package com.example.project1.Service.impl;

import com.example.project1.Dao.UserDao;
import com.example.project1.Dao.UserJwtRepository;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import com.example.project1.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;




@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private static UserDao userDao;

    private UserJwtRepository userJwtRepository;

    @Autowired
    public UserServiceImpl(UserJwtRepository userJwtRepository){
        this.userJwtRepository=userJwtRepository;
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
        int row=userDao.deleteById(id);
        return row;
    }

    @Override
    public List<User> findUser() {
        List<User> userList = userDao.findUser();
        return userList;

    }

    @Override
    public User getUserByNameAndPassword(String name, String password) throws UsernameNotFoundException {
        User user = UserJwtRepository.findByUserNameAndPassword(name, password);
        if(user == null){
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
        List<User> userList = (List<User>) userDao.findByEmail();
        return userList;
    }

    @Override
    public User resetPassword(String password) {
        List<User> userList = (List<User>) userDao.resetPassword();
        return (User) userList;
    }

    @Override
    public int register(UserRegisterRequest userRegisterRequest) {
        User user = (User) findByEmail(userRegisterRequest.getEmail());
        if (user != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        //創建帳號
        return userDao.CreateUser(userRegisterRequest);
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
