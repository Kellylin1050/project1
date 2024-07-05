package com.example.project1.Repository;

import com.example.project1.Entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username = :userName")
    User getUserByUsername(@Param("userName") String userName);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User getUserByEmail(String email);

    //@Query("SELECT u FROM User u WHERE u.username = :userName")
    //User findByUserName(String username);

    @Query("UPDATE User u SET u.password = ?1 WHERE u.id = ?2")
    void resetPassword(@Param("password") String password, @Param("id") Long id);

    @Query("UPDATE User u SET u.email = ?1, u.username = ?2, u.password = ?3, u.phone = ?4 WHERE u.id = ?5")
    void updateUser(@Param("email") String email, @Param("username") String username, @Param("password") String password, @Param("phone") String phone, @Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findById(@Param("id") Long id);

    @Query("DELETE FROM User u WHERE u.id = ?1")
    Integer deleteUserById(@Param("id") Integer id);
}
