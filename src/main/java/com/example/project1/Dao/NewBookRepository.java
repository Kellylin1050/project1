package com.example.project1.Dao;

import com.example.project1.Entity.NewBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewBookRepository extends JpaRepository<NewBook,Integer> {

    Optional<NewBook> findById(Integer id);

    void deleteById(Integer id);

    NewBook findNewBookByTitle(String title);

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

}
