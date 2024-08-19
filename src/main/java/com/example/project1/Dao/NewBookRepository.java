package com.example.project1.Dao;

import com.example.project1.Dto.NewBookRequest;
import com.example.project1.Entity.NewBook;
import com.example.project1.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewBookRepository extends JpaRepository<NewBook,Integer> {
    void deleteById(Integer id);

    @Query("SELECT n FROM NewBook n WHERE n.title = :title")
    NewBook getNewBookByTitle(String title);

    @Modifying
    //@Transactional
    @Query("UPDATE NewBook nb SET nb.title = :title, nb.author = :author, nb.description = :description ,nb.price = :price ,nb.sellprice = :sellprice WHERE nb.id = :id")
    int updateNewBook(Integer id, String title, String author, String description, Integer price, Integer sellprice );
}






