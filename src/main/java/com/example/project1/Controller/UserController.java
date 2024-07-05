package com.example.project1.Controller;

import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private JwtGeneratorService jwtGeneratorService;

    @Autowired
    public UserController(UserService userService, JwtGeneratorService jwtGeneratorService){
        this.userService=userService;
        this.jwtGeneratorService=jwtGeneratorService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> findUser(User user){
        userService.findUser();
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        Integer userId = userService.register(userRegisterRequest);

        User user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }
    /*@PostMapping("/register")
    public ResponseEntity<?> postUser(@RequestBody User user){
        try{
            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            if(user.getUsername() == null || user.getPassword() == null) {
                throw new UsernameNotFoundException("UserName or Password is Empty");
            }
            User userData = userService.getUserByNameAndPassword(user.getUsername(), user.getPassword());
            if(userData == null){
                throw new UsernameNotFoundException("UserName or Password is Invalid");
            }
            return new ResponseEntity<>(jwtGeneratorService.generateToken(user), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/updateUser")
    public ResponseEntity<User> doUpdateUser(User entity){
        userService.updateUser(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @RequestMapping("/editUser/{id}")
    public ResponseEntity<Object> dofindById(@PathVariable Integer id , Model model){
        User user = userService.findById(id);
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userService.updateUser(user);
        User updatedUser =userService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @RequestMapping("/doSaveUser")
    public User doSaveUser(User entity){
        userService.saveUser(entity);
        return entity;
    }

    @RequestMapping("/newUser")
    public String doUserAddUI(){
        return "User_adds";
    }

    @RequestMapping("/deleteUser/{id}")
    public String dodeleteById(Integer id){
        userService.deleteById(id);
        return "delete";
    }

    @RequestMapping("/forgetpassword")
    public String doresetPassword(String password){
        userService.resetPassword(password);
        return "password reset";
    }



}
