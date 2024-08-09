package com.example.project1.Controller;

import com.example.project1.Dao.RoleRepository;
import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Dto.UserResetPasswordRequest;
import com.example.project1.Dto.UserUpdateRequest;
import com.example.project1.Entity.Role;
import com.example.project1.Entity.User;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.UserService;
import com.example.project1.Service.impl.TokenBlacklistService;
import com.example.project1.Service.impl.UserServiceImpl;
import com.example.project1.security.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody ;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "User管理",description = "管理user的相關API")
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserService userService;
    private UserDetailsService userDetailsService;
    private JwtGeneratorService jwtGeneratorService;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    public UserController(UserService userService, JwtGeneratorService jwtGeneratorService) {
        this.userService = userService;
        this.jwtGeneratorService = jwtGeneratorService;
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

    @Operation(
            summary = "查詢使用者",
            description = "返回指定使用者",
            parameters = {
                    @Parameter(name = "username", description = "要查詢的使用者名稱", required = true, schema = @Schema(type = "string"))
            },
            responses = {
                @ApiResponse(responseCode = "200", description = "成功返回使用者"),
                @ApiResponse(responseCode = "404", description = "使用者未找到"),
                @ApiResponse(responseCode = "500", description = "伺服器錯誤")
    }
    )
    //@ApiImplicitParam(name = "username",value = "使用者名稱",dataTypeClass = String.class,paramType = "query")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user") //查
    public ResponseEntity<?> findUser(@RequestParam String username) {
        try {
            User user = userService.findUser(username);
            if (user != null) {
                return ResponseEntity.ok(username); // 返回用户对象
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found"); // 用户未找到
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    /*@PostMapping("/register") //註冊
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        User userId = userService.register(userRegisterRequest);

        User user = userService.findById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }*/

    /*@PostConstruct
    public void initRoles() {
        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(String.join(", ", errors), HttpStatus.BAD_REQUEST);
    }*/


    @Operation(
            summary = "用戶註冊",
            description = "註冊一個新的使用者。",
            //requestBody = @RequestBody(description = "用戶註冊訊息", required = true,content = @Content(schema = @Schema(implementation = UserRegisterRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "使用者註冊成功"),
                    @ApiResponse(responseCode = "400", description = "註冊請求有誤")
            }
    )
    /*@PostMapping("/register")
    public ResponseEntity<String> postUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        //Set<String> role = new HashSet<>(Arrays.asList("USER"));
        try {
            userService.register(userRegisterRequest);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }*/

    @PostMapping("/register")
    public ResponseEntity<?> postUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
        try {
            userRegisterRequest.setRoles(Collections.singleton("USER"));
            // 使用者註冊
            userService.register(userRegisterRequest);
            return new ResponseEntity<>(userRegisterRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


        /*try{
            userService.register(userRegisterRequest);
            return new ResponseEntity<>(userRegisterRequest, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }*/
    }


    @Operation(
            summary = "用戶登入",
            description = "使用使用者名稱和密碼登入，成功後返回 JWT 認證令牌。",
            //requestBody = @RequestBody(description = "用戶登入訊息", required = true, content = @Content(schema = @Schema(implementation = UserLoginRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "登入成功，返回認證令牌"),
                    @ApiResponse(responseCode = "400", description = "使用者名稱或密碼錯誤")
            }
    )
    @PostMapping("/login") //登入
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        User user = userService.login(userLoginRequest);

        if (user == null) {
            logger.info("User {} 不存在", userLoginRequest.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        if (user.getPassword().equals(hashedPassword)) {
            Map<String, String> tokenMap = jwtGeneratorService.generateToken(userLoginRequest);
            String token = tokenMap.get("token");
            return ResponseEntity.ok(new AuthResponse(token, "login successful"));
        } else {
            logger.info("username {} 的密碼不正確", userLoginRequest.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }
    }

        /*User user = userService.login(userLoginRequest);
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        if (user.getPassword().equals(hashedPassword)) {
            String token = String.valueOf(jwtGeneratorService.generateToken(userLoginRequest));
            return ResponseEntity.ok(new AuthResponse(token, "login successful"));
            //return new ResponseEntity<>(jwtGeneratorService.generateToken(userLoginRequest), HttpStatus.OK);
        } else {
            logger.info("email {} 的密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        return ResponseEntity.status(HttpStatus.OK).body(user);
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


    @Operation(
            summary = "用戶登出",
            description = "使當前使用者的 JWT 認證令牌失效。",
            responses = {
                    @ApiResponse(responseCode = "200", description = "登出成功"),
                    @ApiResponse(responseCode = "400", description = "無效的認證令牌")
            }
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/logout")//登出
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtGeneratorService.resolveToken(request);
        System.out.println("Extracted Token: " + token); // Log the token

        boolean isValid = jwtGeneratorService.validateToken(token);
        System.out.println("Is Token Valid: " + isValid); // Log validation result

        if (token != null && jwtGeneratorService.validateToken(token)) {
            tokenBlacklistService.addTokenToBlacklist(token); // 添加到黑名单
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


    @Operation(
            summary = "更新使用者訊息",
            description = "根據提供的訊息更新使用者。",
            //requestBody = @RequestBody(description = "用戶更新訊息", required = true, content = @Content(schema = @Schema(implementation = UserUpdateRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "使用者更新成功"),
                    @ApiResponse(responseCode = "400", description = "更新失敗")
            }
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/updateUser")
    public ResponseEntity<String> doUpdateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        //userService.updateUser(userUpdateRequest);
        //return ResponseEntity.status(HttpStatus.OK).body(userUpdateRequest);
        int result = userService.updateUser(userUpdateRequest);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User update failed");
        }
    }

    @Operation(
            summary = "根據 ID 查詢使用者",
            description = "根據使用者 ID 返回指定使用者的詳細訊息。",
            parameters = {
                    @Parameter(name = "id", description = "使用者 ID", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "成功返回使用者"),
                    @ApiResponse(responseCode = "404", description = "使用者未找到")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/doFindById/{id}")
    public ResponseEntity<Object> dofindById(@PathVariable Integer id, Model model) {
        Optional<User> user = userService.findById(id);
        //if (user == null){
        //    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        //}
        //userService.updateUser(user);
        model.addAttribute("u", user);
        Optional<User> updatedUser = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    /*@RequestMapping("/doSaveUser")
    public User doSaveUser(User entity){
        userService.saveUser(entity);
        return entity;
    }*/
    @Operation(
            summary = "新增使用者",
            description = "新增一個新的使用者。",
           // requestBody = @RequestBody(description = "用戶訊息", required = true, content = @Content(schema = @Schema(implementation = User.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "使用者創建成功"),
                    @ApiResponse(responseCode = "500", description = "伺服器錯誤")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/doSaveUser")
    public ResponseEntity<User> doSaveUser(@RequestBody @Valid User entity) {
        User userId = userService.insertUser(entity);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    /* @RequestMapping("/newUser")     public String doUserAddUI(){
         return "User_adds";
     }
    /*@RequestMapping("/deleteUser/{id}")
     public String dodeleteById(Integer id){
         userService.deleteById(id);
         return "delete";
     }*/

    @Operation(
            summary = "刪除使用者",
            description = "根據使用者 ID 刪除指定使用者。",
            parameters = {
                    @Parameter(name = "id", description = "使用者 ID", required = true, schema = @Schema(type = "integer"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "使用者刪除成功"),
                    @ApiResponse(responseCode = "404", description = "使用者未找到")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{id}")//刪除
    public ResponseEntity<String> dodeleteById(@PathVariable Integer id) {
        if (userService.existsById(id)) {
            userService.deleteById(id);
            return ResponseEntity.ok("delete");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @Operation(
            summary = "重置密碼",
            description = "根據提供的重置密碼請求重置使用者的密碼。",
            //requestBody = @RequestBody(description = "重置密碼請求訊息", required = true, content = @Content(schema = @Schema(implementation = UserResetPasswordRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "密碼重置成功"),
                    @ApiResponse(responseCode = "400", description = "使用者未找到或重置密碼失敗")
            }
    )
    @PostMapping("/forgetpassword") //忘記密碼
    public ResponseEntity<String> doresetPassword(@RequestBody UserResetPasswordRequest userResetPasswordRequest) {
        int result = userService.resetPassword(userResetPasswordRequest);
        if (result == 1) {
            return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }

    @Operation(
            summary = "403錯誤頁面",
            description = "返回403錯誤頁面"
    )
    @RequestMapping("/denied")
    public ResponseEntity<String> error403(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("403");
    }

}

    /*@RequestMapping("/forgetpassword")
    public String doresetPassword(String password){
        userService.resetPassword(password);
        return "password reset";
    }*/


