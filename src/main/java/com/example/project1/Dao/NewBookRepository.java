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

    //NewBook getNewBookById(Integer id);

    void deleteById(Integer id);

    @Query("SELECT n FROM NewBook n WHERE n.title = :title")
    NewBook getNewBookByTitle(String title);

    @Modifying
    @Transactional
    @Query("UPDATE NewBook nb SET nb.title = :title, nb.author = :author, nb.description = :description ,nb.price = :price ,nb.sellprice = :sellprice WHERE nb.id = :id")
    int updateNewBook(Integer id, String title, String author, String description, Integer price, Integer sellprice );
}



/*
    @Update("update new_book set Title=#{title},=#{description} =#{price} =#{sellprice} where id=#{id}")
    public Integer updateNewBook(NewBook entity) {
        return null;
    }

    @Select("select * from new_book where id=#{id}")
    public NewBook findById(Integer id) {
        return null;
    }

    @Insert("insert into new_book(title,author,description,price,sellprice) " +
            "values(#{title} =#{description} =#{price} =#{sellprice}")
    public int insertNewBook(NewBook entity) {
        return 0;
    }

    @Delete("delete from new_book where id=#{id}")
    public int deleteById(Integer id) {
        return 0;
    }

    @Select("select * from new_book")
    public List<NewBook> findNewBook() {
        return null;
      }
*/


