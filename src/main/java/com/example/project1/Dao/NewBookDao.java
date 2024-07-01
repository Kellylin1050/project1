package com.example.project1.Dao;

import com.example.project1.Entity.NewBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public class NewBookDao {

    @Update("update new_book set Title=#{title},=#{description} =#{price} =#{sellprice} where id=#{id}")
    public Integer updateNewBook(NewBook entity) {
        return null;
    }

    @Select("select * from new_book where id=#{id}")
    public NewBook findById(Integer id) {
        return null;
    }

    @Insert("insert into new_book(title,author,call_Number,barcode,exh_fr,exh_end,revise_date,act_yn) " +
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


}
