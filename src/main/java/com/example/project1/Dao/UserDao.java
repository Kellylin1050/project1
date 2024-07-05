package com.example.project1.Dao;

import com.example.project1.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Optional;

@Mapper
public interface UserDao  {
    //@Insert("insert into user(email,user_name,password,enabled) values(#{email},#{user_name},#{password},#{phone},#{enabled})")
    //int insertUser(User user);

    //@Insert("insert into user(email,name,password,enabled) values(#{email},#{name},#{password},#{enabled})")
    //int CreateUser(UserRegisterRequest userRegisterRequest);

    //@Update("update user set Email=#{email},#{user_name},#{password},#{phone},#{enabled} where id=#{id}")
    //Integer updateUser(User entity);

    @Select("select * from user where id=#{id}")
    Optional<User> findById(Long id);

   // @Delete("delete from user where id=#{id}")
   // Integer deleteById(Integer id);

    @Select("select * from user where Email=#{email}")
    User getUserByEmail(String email);

   // @Select("select * from user where Email=#{email}")
    //static User findByEmail() {
     //   return null;
    //}

    @Update("update user set #{password} where id=#{id}")
    User resetPassword();

    //@Select("select * from user where user_name=#{username}")
    //String findByUsername(String username);
}
