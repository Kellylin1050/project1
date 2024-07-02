package com.example.project1.Dao;

import com.example.project1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJwtRepository extends JpaRepository<User, String> {
    public static User findByUserNameAndPassword(String userName, String password) {
        return null;
    }
}
