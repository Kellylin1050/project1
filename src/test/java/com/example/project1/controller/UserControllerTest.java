package com.example.project1.controller;

import com.example.project1.Dao.UserRepository;
import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import com.example.project1.Project1Application;
import com.example.project1.Service.JwtGeneratorService;
import com.example.project1.Service.UserService;
import com.example.project1.Service.impl.TokenBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.internal.Classes;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.client.match.JsonPathRequestMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes={Project1Application.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
//@MapperScan(basePackages = "com.example.project1.Dao.UserDao")
//@ContextConfiguration(locations ="classpath:application.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtGeneratorService jwtGeneratorService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testMapper() {
        User u1 = userRepository.findById(1);
        System.out.println(u1.getName());
    }
    @Test//是否能夠去註冊一個新帳號
    public void register_success() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test3@gmail.com");
        userRegisterRequest.setPassword("1234");
        userRegisterRequest.setName("kkkk");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo("test3@gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", notNullValue()));

        // 檢查資料庫中的密碼不為明碼
        User user = userRepository.getUserByEmail(userRegisterRequest.getEmail());
        assertNotEquals(userRegisterRequest.getPassword(), user.getPassword());
    }

    @Test //不是 email 格式的值的話是否能夠擋下來
    public void register_invalidEmailFormat() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("3gd8e7q34l9");
        userRegisterRequest.setPassword("123");
        userRegisterRequest.setName("kkkk");


        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test //使用了一個已經註冊過的 email 來註冊是否能夠擋下來
    public void register_emailAlreadyExist() throws Exception {
        // 先註冊一個帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("save@gmail.com");
        userRegisterRequest.setPassword("245857");
        userRegisterRequest.setName("owief");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        // 再次使用同個 email 註冊
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    /*@Test
    public void testGetUserA() throws Exception {
        ResultActions negativeCase = mockMvc.perform(userDao.findUser("/users/user")
                .contentType(MediaType.APPLICATION_JSON));
        negativeCase.andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
    }*/



    // 登入
   /* @Test //是否能夠成功的去登入
    public void login_success() throws Exception {
        // 先註冊新帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test5@gmail.com");
        userRegisterRequest.setPassword("12345");
        userRegisterRequest.setName("djfkftjf");

        register(userRegisterRequest);

        // 再測試登入功能
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail(userRegisterRequest.getEmail());
        userLoginRequest.setPassword(userRegisterRequest.getPassword());

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", equalTo(userRegisterRequest.getPassword())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo(userRegisterRequest.getEmail())));

    }*/

    @Test
    public void login_success() throws Exception {
        // 先註冊新帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test12@gmail.com");
        userRegisterRequest.setPassword("12345");
        userRegisterRequest.setName("mmm");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // 再測試登入功能
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail(userRegisterRequest.getEmail());
        userLoginRequest.setPassword(userRegisterRequest.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
                //.andExpect(MockMvcResultMatchers.jsonPath("$.token").exists()); // 假設登入成功後返回包含 token 的 JSON
    }

   /* @Test
    public void login_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"test1@example.com\", \"password\": \1234\" }"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists()); // 假設登入成功後返回包含 token 的 JSON
    }*/

    @Test //使用者的密碼輸入錯誤是否能夠擋下來
    public void login_wrongPassword() throws Exception {
        // 先註冊新帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test10@gmail.com");
        userRegisterRequest.setPassword("1234");
        userRegisterRequest.setName("aaatgs");

        register(userRegisterRequest);

        // 測試密碼輸入錯誤的情況
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail(userRegisterRequest.getEmail());
        userLoginRequest.setPassword("unknown");

        String json = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test //email 格式輸入不正確的情況是否能夠擋下來
    public void login_invalidEmailFormat() throws Exception {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("hkbudsr324");
        userLoginRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test //輸入了一個從來沒有註冊過的帳號是否可以擋下來
    public void login_emailNotExist() throws Exception {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setEmail("unknown@gmail.com");
        userLoginRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void testLogout() throws Exception{
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MTJAZ21haWwuY29tIiwiaWF0IjoxNzIxNjM5OTkwfQ.qzTW25waS67KL22p9atpFQJsU7a2viZzRD32k0MMCg4";
        //jwtGeneratorService.validateToken(token);
        //tokenBlacklistService.addTokenToBlacklist(token);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/logout")
                .header("Authorization","Bearer" + token)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("Successful logged out"));

    }

    @Test //???????
    public void testdoUpdateUser() throws Exception{
        User user = new User();
        user.setId(32);
        user.setEmail("test374@gmail.com");
        user.setName("fjdig");
        user.setPassword("987654");
        user.setPhone("0937485123");
        userService.updateUser(user);
        String json = objectMapper.writeValueAsString(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/updateUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
               // .andExpect(MockMvcResultMatchers.jsonPath("$.name", notNullValue()))
               // .andExpect(MockMvcResultMatchers.jsonPath("$.email", notNullValue()))
               // .andExpect(MockMvcResultMatchers.jsonPath("$.password", notNullValue()));
    }

    @Test
    public void testdoFindById() throws Exception{
        User user = new User();
        user.setId(1);
        userService.findById(1);
        String json = objectMapper.writeValueAsString(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/users/doFindById/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void testdoSaveUser()throws Exception{
        User user = new User();
        user.setId(2);
        user.setEmail("save@gmail.com");
        user.setName("owief");
        user.setPassword("245857");
        userService.insertUser(user);
        String json = objectMapper.writeValueAsString(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/doSaveUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }
    @Test//?????????
    public void testdoDeleteById()throws Exception{
        userService.deleteById(37);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/users/deleteUser/{id}",39);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
                //.andExpect(content().string("delete"));
    }

    @Test
    public void testdoResetPassword() throws Exception{
        User user = new User();
        user.setId(33);
        user.setName("difue");
        user.setEmail("test4@gmail.com");
        user.setPassword("5j084jf");
        userService.resetPassword(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/forgetpassword");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }



    //提煉註冊程式
    private void register(UserRegisterRequest userRegisterRequest) throws Exception {
        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));
    }
}
