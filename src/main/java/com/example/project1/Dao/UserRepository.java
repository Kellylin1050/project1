package com.example.project1.Dao;

import com.example.project1.Dto.UserRegisterRequest;
import com.example.project1.Entity.User;
import org.apache.ibatis.annotations.Param;
import org.hibernate.sql.Insert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Integer>, JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :userName")
    User getUserByUsername(@Param("userName") String userName);

    User findById(int id);
    Integer deleteById(int id);

    User getUserByEmail(String email);


    //@Modifying
    //@Query("")
    //User CreateUser(UserRegisterRequest userRegisterRequest);



}