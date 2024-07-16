package com.example.project1.Controller;

import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private UserDetailsService userDetailsService;
    private JwtGeneratorService jwtGeneratorService;

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
        try {
            if(userLoginRequest.getEmail() == null || userLoginRequest.getPassword() == null) {
                throw new UsernameNotFoundException("Email or Password is Empty");
            }
            User userData = userService.getUserByNameAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());
            if(userData == null){
                throw new UsernameNotFoundException("UserName or Password is Invalid");
            }
            return new ResponseEntity<>(jwtGeneratorService.generateToken(userLoginRequest), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    /*@PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user = userService.login(userLoginRequest);
        return ResponseEntity.ok(user);
    }*/


    @PostMapping("/updateUser") //新增
    public ResponseEntity<User> doUpdateUser(User entity){
        userService.updateUser(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @RequestMapping("/editUser/{id}")  //改
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
    public ResponseEntity<Void> dodeleteById(@PathVariable Integer id) {
        Integer deleteCount = Integer.valueOf(userService.deleteById(id));
        if (deleteCount == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/forgetpassword") //忘記密碼
    public ResponseEntity<String> doresetPassword(@RequestParam User newPassword) {
        User updateCount = userService.resetPassword(newPassword);
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


