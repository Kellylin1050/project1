package com.example.project1.Dao;

import com.example.project1.Entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public class UserDao {
    @Insert("insert into users(email,user_name,password,enabled) values(#{email},#{user_name},#{password},#{phone},#{enabled})")
    public int insertUser(User entity) {
        return 0;
    }

    @Update("update users set Email=#{email},#{user_name},#{password},#{phone},#{enabled} where id=#{id}")
    public Integer updateUser(User entity) {
        return null;
    }

    @Select("select * from users where id=#{id}")
    public User findById(Integer id) {
        return null;
    }

    @Delete("delete from users where id=#{id}")
    public int deleteById(Integer id) {
        return 0;
    }

    @Select("select * from users")
    public List<User> findUser() {
        return null;
    }

}
