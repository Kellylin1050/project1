package com.example.project1.Service;

import com.example.project1.Entity.NewBook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface NewBookService {
    NewBook updateNewBook(NewBook entity);
    Optional<NewBook> findById(Integer id);
    NewBook saveNewBook(NewBook entity);
    String deleteById(Integer id);
    List<NewBook> findNewBook(String title);
}
