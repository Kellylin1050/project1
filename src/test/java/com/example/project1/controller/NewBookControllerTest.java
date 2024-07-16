package com.example.project1.controller;

import com.example.project1.Dao.NewBookRepository;
import com.example.project1.Project1Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testFindNewBook() throws Exception {
        String newBook = "Test Book";
        newBookRepository.findNewBookByTitle(newBook);
        String json = objectMapper.writeValueAsString(newBook);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/NewBook/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }

}
