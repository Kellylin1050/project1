package com.example.project1.Dao;

import com.example.project1.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(String username);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name, u.phone = :phone WHERE u.id = :id")
    int updateUser(Integer id, String name, String phone);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    int resetPassword(Integer id, String password);
    @Query("SELECT u FROM User u")
    List<User> findAllUser();
    //Optional<User> findById(Integer id);
    Integer deleteById(int id);

    User getUserByEmail(String email);

}