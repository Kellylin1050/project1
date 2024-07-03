package com.example.project1.Dao;

import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public class UserDao {
    @Insert("insert into user(email,user_name,password,enabled) values(#{email},#{user_name},#{password},#{phone},#{enabled})")
    public int insertUser(User entity){
        return 0;
    }

    @Insert("insert into user(email,name,password,enabled) values(#{email},#{name},#{password},#{enabled})")
    public int CreateUser(UserRegisterRequest entity){
        return 0;
    }
    @Update("update user set Email=#{email},#{user_name},#{password},#{phone},#{enabled} where id=#{id}")
    public Integer updateUser(User entity) {
        return null;
    }

    @Select("select * from user where id=#{id}")
    public User findById(Integer id) {
        return null;
    }

    @Delete("delete from user where id=#{id}")
    public int deleteById(Integer id) {
        return 0;
    }

    @Select("select * from user")
    public List<User> findUser() {
        return null;
    }

    @Select("select * from user where Email=#{email}")
    public User findByEmail(){
        return null;
    }

    @Update("update user set #{password} where id=#{id}")
    public User resetPassword(){
        return null;
    }

}
