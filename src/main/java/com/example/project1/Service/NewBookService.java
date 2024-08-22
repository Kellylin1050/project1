package com.example.project1.Service;

import com.example.project1.Dto.NewBookRequest;
import com.example.project1.Entity.NewBook;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface NewBookService {
    int updateNewBook(NewBookRequest newBookRequest);

    NewBook saveNewBook(NewBook entity);
    String deleteById(Integer id);
    NewBook getNewBookByTitle(String title);

    boolean existsById(Integer id);

    List<NewBook> findAllBook();
}
