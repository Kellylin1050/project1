package com.example.project1.Service;

import com.example.project1.Entity.NewBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NewBookService {
    Integer updateNewBook(NewBook entity);
    NewBook findById(Integer id);
    Integer saveNewBook(NewBook entity);
    Integer deleteById(Integer id);
    List<NewBook> findNewBook();
}
