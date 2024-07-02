package com.example.project1.RowMapper;

import com.example.project1.Entity.NewBook;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewBookRowMapper implements RowMapper<NewBook> {

    @Override
    public NewBook mapRow(ResultSet rs, int rowNum) throws SQLException {
        NewBook newbook = new NewBook();
        newbook.setTitle(rs.getString("Title"));
        newbook.setAuthor(rs.getString("Author"));
        newbook.setDescription(rs.getString("Description"));
        newbook.setPrice(rs.getInt("Price"));
        newbook.setSellprice(rs.getInt("Sellprice"));
        newbook.setId(rs.getInt("book_id"));

        return newbook;
    }
}
