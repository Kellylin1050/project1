package com.example.project1.Service.impl;

import com.example.project1.Dao.RoleRepository;
import com.example.project1.Dao.UserJwtRepository;
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
    private UserJwtRepository userJwtRepository;

    @Autowired
    private JwtGeneratorService jwtGeneratorService;

    @Autowired
    public UserServiceImpl(UserJwtRepository userJwtRepository,UserRepository userRepository) {
        this.userJwtRepository = userJwtRepository;
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
        //user1.setRoles((Set<Role>) roleRepository.findByName("ROLE_USER"));

        //Set<Role> roles = new HashSet<>();
        //roles.add(roleRepository.findByName("USER"));
        // user1.setRoles(roles);

        Role userRole = roleRepository.findByName("ROLE_USER");
        user1.setRoles(Collections.singleton(userRole));


        //Role role = roleRepository.findByName(Role.RoleName.ROLE_USER);
        //user1.setRoles((Set<Role>) role);
        //Role role = roleRepository.findByName("ROLE_USER");
        //user1.setRoles((Set<Role>) role);
        return userRepository.save(user1);



        /* Role userRole = new Role();
        Set<Role> roles = new HashSet<>();
        userRole.setName("USER");
        roles.add(userRole);
        user1.setRoles(roles);

        for (String roleName : roleNames){
            Role role =roleRepository.findByName(roleName);
            roles.add(role);
        }
        return userRepository.save(user1);

        if(userRepository.save(user1)!=null) {
            User user2 = new User();
            Authentication auth = new UsernamePasswordAuthenticationToken(user2, "User");
            SecurityContextHolder.getContext().setAuthentication(auth);
            return user2;
        }
        return null;*/

    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userRepository.getUserByUsername(userLoginRequest.getUsername());
       // Role role = roleRepository.findByName("ROLE_USER");
       // user.setRoles((Set<Role>) role);
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


