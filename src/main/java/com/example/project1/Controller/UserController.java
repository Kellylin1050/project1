package com.example.project1.Controller;

import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.UserService;
import com.example.project1.Service.impl.TokenBlacklistService;
import com.example.project1.Service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserService userService;
    private UserDetailsService userDetailsService;
    private JwtGeneratorService jwtGeneratorService;
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    public UserController(UserService userService, JwtGeneratorService jwtGeneratorService){
        this.userService=userService;
        this.jwtGeneratorService=jwtGeneratorService;
    }
    /*
    @GetMapping("/unrestricted")
    public ResponseEntity<?> getMessage() {
        return new ResponseEntity<>("Hai this is a normal message..", HttpStatus.OK);
    }

    @GetMapping("/restricted")
    public ResponseEntity<?> getRestrictedMessage() {
        return new ResponseEntity<>("This is a restricted message", HttpStatus.OK);
    }*/

    @GetMapping("/user") //查
    public ResponseEntity<String> findUser(String user){
        userService.findUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /*@PostMapping("/register") //註冊
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        User userId = userService.register(userRegisterRequest);

        User user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }*/
    @PostMapping("/register")
    public ResponseEntity<?> postUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
        try{
            userService.register(userRegisterRequest);
            return new ResponseEntity<>(userRegisterRequest, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login") //登入
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        User user = userService.login(userLoginRequest);
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        if (user.getPassword().equals(hashedPassword)) {
            return new ResponseEntity<>(jwtGeneratorService.generateToken(userLoginRequest), HttpStatus.OK);
        } else {
            logger.info("email {} 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        /*return ResponseEntity.status(HttpStatus.OK).body(user);
        try {
            if(user.getEmail() == null || user.getPassword() == null) {
                throw new UsernameNotFoundException("Email or Password is Empty");
            }
            User userData = userService.getUserByNameAndPassword(user.getEmail(), user.getPassword());
            if(userData == null){
                throw new UsernameNotFoundException("UserName or Password is Invalid");
            }
            return new ResponseEntity<>(jwtGeneratorService.generateToken(userLoginRequest), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }*/
    }


    @PostMapping("/logout")//登出
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtGeneratorService.resolveToken(request);

        if (token != null && jwtGeneratorService.validateToken(token)) {
            tokenBlacklistService.addTokenToBlacklist(token); // 将令牌添加到黑名单
            return ResponseEntity.ok("Successfully logged out");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
    }
    /*@PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user = userService.login(userLoginRequest);
        return ResponseEntity.ok(user);
    }*/



    @PostMapping("/updateUser")
    public ResponseEntity<User> doUpdateUser(User entity){
        userService.updateUser(entity);
        return ResponseEntity.status(HttpStatus.OK).body(entity);
    }

    @GetMapping("/doFindById/{id}")
    public ResponseEntity<Object> dofindById(@PathVariable Integer id ,Model model){
        Optional<User> user = userService.findById(id);
        //if (user == null){
        //    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        //}
        //userService.updateUser(user);
        model.addAttribute("u",user);
        Optional<User> updatedUser =userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    /*@RequestMapping("/doSaveUser")
    public User doSaveUser(User entity){
        userService.saveUser(entity);
        return entity;
    }*/
    @PostMapping("/doSaveUser")
    public ResponseEntity<User> doSaveUser(@RequestBody @Valid User entity) {
        User userId = userService.insertUser(entity);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    /* @RequestMapping("/newUser")
     public String doUserAddUI(){
         return "User_adds";
     }
    /*@RequestMapping("/deleteUser/{id}")
     public String dodeleteById(Integer id){
         userService.deleteById(id);
         return "delete";
     }*/
    @DeleteMapping("/deleteUser/{id}")  //刪除
    public ResponseEntity<String> dodeleteById(@PathVariable Integer id) {
        Integer deleteCount = Integer.valueOf(userService.deleteById(id));
        if (deleteCount == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body("delete");
    }
    @PostMapping("/forgetpassword") //忘記密碼
    public ResponseEntity<String> doresetPassword(@RequestParam User Password) {
        User updateCount = userService.resetPassword(Password);
        if (updateCount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok("Password reset successfully");
    }
}

    /*@RequestMapping("/forgetpassword")
    public String doresetPassword(String password){
        userService.resetPassword(password);
        return "password reset";
    }*/


