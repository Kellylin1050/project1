package com.example.project1.controller;

import com.example.project1.Dao.NewBookRepository;
import com.example.project1.Entity.NewBook;
import com.example.project1.Project1Application;
import com.example.project1.Service.NewBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.N;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

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
    public void testFindNewBook() throws Exception {
        String newBook = "The idea of you";
        newBookRepository.findNewBookByTitle(newBook);
        String json = objectMapper.writeValueAsString(newBook);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/NewBook/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void testdoUpdateNewBook() throws Exception{
        NewBook book = new NewBook();
        book.setId(1);
        book.setTitle("The idea of you");
        book.setAuthor("Robinne Lee");
        book.setPrice(200);
        book.setSellprice(576);
        newBookService.updateNewBook(book);
        String json = objectMapper.writeValueAsString(book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/NewBook/doUpdateNewBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void testdoFindById() throws Exception{
        NewBook book = new NewBook();
        book.setId(1);
        book.setTitle("The idea of you");
        newBookService.findById(1);
        String json = objectMapper.writeValueAsString(book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/NewBook/doFindById/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

    }

    @Test
    public void testdoSaveNewBook() throws Exception{
        NewBook book = new NewBook();
        book.setId(2);
        book.setTitle("save NewBook");
        newBookService.saveNewBook(book);
        String json = objectMapper.writeValueAsString(book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/NewBook/doSaveNewBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    public void testdoDeleteById() throws Exception{
        newBookService.deleteById(4);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/NewBook/delete/{id}",4);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

}
