package com.example.project1.controller;

import com.example.project1.Dao.UserRepository;
import com.example.project1.Dto.UserLoginRequest;
import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Dto.UserResetPasswordRequest;
import com.example.project1.Dto.UserUpdateRequest;
import com.example.project1.Entity.User;
import com.example.project1.Project1Application;
import com.example.project1.Service.UserService;
import com.example.project1.Service.impl.TokenBlacklistService;
import com.example.project1.security.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.internal.util.Contracts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
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
    private TokenBlacklistService tokenBlacklistService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    /*@Test
    public void givenWrongEntityScanApplicationFixed_whenBootstrap_thenRepositoryBeanShouldBePresentInContext() {
        SpringApplication app = new SpringApplication(User.class);
        app.setAdditionalProfiles("test");
        ConfigurableApplicationContext context = app.run();
        UserRepository repository = context
                .getBean(UserRepository.class);
        assertThat(repository).isNotNull();
    }*/
    @Test//是否能夠去查詢一個新帳號
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void getUser_success() throws Exception{
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("john.doe@example.com");
        userRegisterRequest.setPassword("1234567");
        userRegisterRequest.setName("john");
        userRegisterRequest.setUsername("john");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/user")
                        .param("username", "john"))
                .andExpect(status().isOk())
                .andExpect(content().string(userRegisterRequest.getUsername()));
    }
    @Test//是否能夠去註冊一個新帳號
    public void register_success() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test50@gmail.com");
        userRegisterRequest.setPassword("12345");
        userRegisterRequest.setName("qqq");
        userRegisterRequest.setUsername("qqq");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", equalTo("test50@gmail.com")))
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
        userRegisterRequest.setUsername("kkkk");


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
        userRegisterRequest.setUsername("owief");

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

    @Test
    public void login_success() throws Exception {
        // 先註冊新帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test12@gmail.com");
        userRegisterRequest.setPassword("12345");
        userRegisterRequest.setName("mmm");
        userRegisterRequest.setUsername("mmm");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // 再測試登入功能
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername(userRegisterRequest.getUsername());
        userLoginRequest.setPassword(userRegisterRequest.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists()); // 假設登入成功後返回包含 token 的 JSON
    }

    @Test //使用者的密碼輸入錯誤是否能夠擋下來
    public void login_wrongPassword() throws Exception {
        // 先註冊新帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test10@gmail.com");
        userRegisterRequest.setPassword("1234");
        userRegisterRequest.setName("aaatgs");
        userRegisterRequest.setUsername("aaatgs");

        register(userRegisterRequest);

        // 測試密碼輸入錯誤的情況
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername(userRegisterRequest.getUsername());
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
        userLoginRequest.setUsername("hkbudsr324");
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
        userLoginRequest.setUsername("unknown@");
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
    public void testLogout() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("test40@gmail.com");
        userRegisterRequest.setPassword("12345678");
        userRegisterRequest.setName("xxx");
        userRegisterRequest.setUsername("xxx");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // 登錄用戶
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername(userRegisterRequest.getUsername());
        userLoginRequest.setPassword(userRegisterRequest.getPassword());

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 獲取 JWT token
        String loginResponse = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(loginResponse, AuthResponse.class);
        String token = authResponse.getToken();
        assertNotNull(token, "JWT token should not be null");

        // 執行登出請求，並確保登出成功
        mockMvc.perform(MockMvcRequestBuilders.post("/users/logout")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully logged out"));

        // 確保 JWT token 被加入黑名單
        //tokenBlacklistService.addTokenToBlacklist(token);
        assertTrue(tokenBlacklistService.isTokenBlacklisted(token), "Token should be in the blacklist");

        // 驗證登出後的行為
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(11);
        userUpdateRequest.setName("fjdig");
        userUpdateRequest.setPhone("0937485123");
        userUpdateRequest.setUsername("fjdig");

        String json = objectMapper.writeValueAsString(userUpdateRequest);
        MockHttpServletRequestBuilder updateRequestBuilder = MockMvcRequestBuilders
                .post("/users/updateUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token);

        mockMvc.perform(updateRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testdoUpdateUser() throws Exception{
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(11);
        userUpdateRequest.setName("fjdig");
        userUpdateRequest.setPhone("0937485123");
        userUpdateRequest.setUsername("fjdig");
        //userService.updateUser(userUpdateRequest);
        String json = objectMapper.writeValueAsString(userUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/updateUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
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
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testdoSaveUser()throws Exception{
        User user = new User();
        user.setEmail("save1234@gmail.com");
        user.setName("2349808");
        user.setUsername("dsadasd");
        user.setPassword("245857");
        user.setPhone("0947382647");
        String json = objectMapper.writeValueAsString(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/doSaveUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testdoDeleteById()throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/users/deleteUser/{id}",2);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("delete"));
    }

    @Test
    public void testdoResetPassword() throws Exception{
        UserResetPasswordRequest userResetPasswordRequest = new UserResetPasswordRequest();
        userResetPasswordRequest.setId(12);
        userResetPasswordRequest.setPassword("448r9f693v");
        userService.resetPassword(userResetPasswordRequest);
        String json = objectMapper.writeValueAsString(userResetPasswordRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/forgetpassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
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
