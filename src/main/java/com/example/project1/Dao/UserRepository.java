package com.example.project1.Dao;

import com.example.project1.Dto.UserUpdateRequest;
import com.example.project1.Entity.User;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Integer>, JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(String username);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name, u.username = :username, u.phone = :phone WHERE u.id = :id")
    int updateUser(Integer id, String name, String username, String phone);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    int resetPassword(Integer id, String password);


    User findById(int id);
    Integer deleteById(int id);

    User getUserByEmail(String email);




    //@Modifying
    //@Query("")
    //User CreateUser(UserRegisterRequest userRegisterRequest);



}