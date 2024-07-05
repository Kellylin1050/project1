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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtGeneratorService jwtGeneratorService;

    @Autowired
    public UserController(UserService userService, JwtGeneratorService jwtGeneratorService){
        this.userService = userService;
        this.jwtGeneratorService = jwtGeneratorService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> findUser(@PathVariable User user){
        userService.findByUsername(user.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Optional<User>> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        Long userId = Long.valueOf(userService.register(userRegisterRequest));

        Optional<User> user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        try {
            if(userLoginRequest.getEmail() == null || userLoginRequest.getPassword() == null) {
                throw new UsernameNotFoundException("UserName or Password is Empty");
            }
            User userData = userService.getUserByNameAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());
            if(userData == null){
                throw new UsernameNotFoundException("UserName or Password is Invalid");
            }
            return ResponseEntity.ok(jwtGeneratorService.generateToken(userData));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> doUpdateUser(@RequestBody @Valid User entity) {
        Integer updateCount = userService.updateUser(entity);
        if (updateCount == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(entity);
    }

    @GetMapping("/editUser/{id}")
    public ResponseEntity<User> dofindById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/doSaveUser")
    public ResponseEntity<User> doSaveUser(@RequestBody @Valid User entity) {
        Integer userId = userService.saveUser(entity);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> dodeleteById(@PathVariable Integer id) {
        Integer deleteCount = userService.deleteById(id);
        if (deleteCount == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgetpassword")
    public ResponseEntity<String> doresetPassword(@RequestParam String newPassword) {
        User updateCount = userService.resetPassword(newPassword);
        if (updateCount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok("Password reset successfully");
    }
}
/*    private UserService userService;
    private JwtGeneratorService jwtGeneratorService;

    @Autowired
    public UserController(UserService userService, JwtGeneratorService jwtGeneratorService){
        this.userService=userService;
        this.jwtGeneratorService=jwtGeneratorService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> findUser(@PathVariable User user){
        userService.findByUsername(user.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<Optional<User>> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        Long userId = Long.valueOf(userService.register(userRegisterRequest));

        Optional<User> user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
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
    public ResponseEntity<User> doUpdateUser(@RequestBody User entity){
        userService.updateUser(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @GetMapping("/editUser/{id}")
    public ResponseEntity<Optional<User>> dofindById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/doSaveUser")
    public ResponseEntity<User> doSaveUser(@RequestBody User entity){
        userService.saveUser(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }


    @RequestMapping("/newUser")
    public String doUserAddUI(){
        return "User_adds";
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> dodeleteById(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @RequestMapping("/forgetpassword")
    public String doresetPassword(String password){
        userService.resetPassword(password);
        return "password reset";
    }
*/
/*@PostMapping("/register")
    public ResponseEntity<?> postUser(@RequestBody User user){
        try{
            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }*/