package com.example.project1.Service.impl;

import com.example.project1.Dao.NewBookRepository;
import com.example.project1.Dto.NewBookRequest;
import com.example.project1.Entity.NewBook;
import com.example.project1.Entity.User;
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
    public int updateNewBook(NewBookRequest newBookRequest) {
       return newBookRepository.updateNewBook(newBookRequest.getId(),newBookRequest.getTitle(),newBookRequest.getAuthor(),newBookRequest.getDescription(),newBookRequest.getPrice(),newBookRequest.getSellprice());
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
    public NewBook getNewBookByTitle(String title) {
        return newBookRepository.getNewBookByTitle(title);

    }
    @Override
    public List<NewBook> findAllBook() {
        return newBookRepository.findAllBook();
    }

    @Override
    public boolean existsById(Integer id) {
        return newBookRepository.existsById(id);
    }

}
