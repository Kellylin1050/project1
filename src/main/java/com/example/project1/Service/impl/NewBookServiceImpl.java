package com.example.project1.Service.impl;

import com.example.project1.Dao.NewBookDao;
import com.example.project1.Entity.NewBook;
import com.example.project1.Service.NewBookService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class NewBookServiceImpl implements NewBookService {
    private static final Logger log= (Logger) LoggerFactory.getLogger(NewBookServiceImpl.class);
    @Autowired
    private NewBookDao newBookDao;
    @Override
    public Integer updateNewBook(NewBook entity) {
        return newBookDao.updateNewBook(entity);
    }
    @Override
    public NewBook findById(Integer id) {
        return newBookDao.findById (id);// In the future, the query results can be stored in the cache in the business logic layer;
    }
    @Override
    public Integer saveNewBook(NewBook entity) {
        return newBookDao.insertNewBook(entity);
    }
    @Override
    public Integer deleteById(Integer id) {
        int rows=newBookDao.deleteById(id);
        return rows;
    }
    @Override
    public List<NewBook> findNewBook() {
        long t1=System.currentTimeMillis();
        List<NewBook> newBookList=newBookDao.findNewBook();
        long t2=System.currentTimeMillis();
        return newBookList;
    }
}
