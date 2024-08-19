package com.example.project1.controller;

import com.example.project1.Controller.NewBookController;
import com.example.project1.Dao.NewBookRepository;
import com.example.project1.Dto.NewBookRequest;
import com.example.project1.Entity.NewBook;
import com.example.project1.Entity.Role;
import com.example.project1.Project1Application;
import com.example.project1.Service.NewBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.N;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes={Project1Application.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class NewBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewBookRepository newBookRepository;

    @Autowired
    private NewBookService newBookService;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testFindNewBook() throws Exception {
        NewBookRequest bookRequest = new NewBookRequest();
        bookRequest.setTitle("The idea of you");
        bookRequest.setAuthor("Robinne");
        bookRequest.setPrice(942);
        bookRequest.setSellprice(784);
        String json = objectMapper.writeValueAsString(bookRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/NewBook/doSaveNewBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

        String title = "The idea of you";

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders
                .get("/NewBook/book")
                .param("title", title);

        mockMvc.perform(requestBuilder1)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(title));
    }
    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testdoUpdateNewBook() throws Exception{
        NewBookRequest bookRequest = new NewBookRequest();
        bookRequest.setId(3);
        bookRequest.setTitle("test");
        bookRequest.setAuthor("test");
        bookRequest.setPrice(394);
        bookRequest.setSellprice(937);

        String json = objectMapper.writeValueAsString(bookRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/NewBook/doUpdateNewBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testdoSaveNewBook() throws Exception{
        NewBookRequest bookRequest= new NewBookRequest();
        bookRequest.setTitle("save NewBook");
        bookRequest.setAuthor("Save");
        bookRequest.setPrice(493);
        bookRequest.setSellprice(938);

        String json = objectMapper.writeValueAsString(bookRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/NewBook/doSaveNewBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testdoDeleteById() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/NewBook/delete/{id}",2);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

}
