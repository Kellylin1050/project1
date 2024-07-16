package com.example.project1.Service.impl;

import com.example.project1.Dao.NewBookRepository;
import com.example.project1.Entity.NewBook;
import com.example.project1.Service.NewBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewBookServiceImpl implements NewBookService {
    //private static final Logger log= (Logger) LoggerFactory.getLogger(NewBookServiceImpl.class);
    @Autowired
    private NewBookRepository newBookRepository;
    @Override
    public NewBook updateNewBook(NewBook entity) {
        return newBookRepository.save(entity);
    }
    @Override
    public Optional<NewBook> findById(Integer id) {
        return newBookRepository.findById (id);
    }
    @Override
    public NewBook saveNewBook(NewBook entity) {
        return newBookRepository.save(entity);
    }
    @Override
    public String deleteById(Integer id) {
        newBookRepository.deleteById(id);
        return "delete";
    }
    @Override
    public List<NewBook> findNewBook(String title) {
        List<NewBook> newBookList= (List<NewBook>) newBookRepository.findNewBookByTitle(title);
        return newBookList;
    }
}
