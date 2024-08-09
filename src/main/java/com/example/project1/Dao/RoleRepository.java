package com.example.project1.Dao;

import com.example.project1.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.username = :username")
    List getRoleByUserName(String username);

    Role findByName(String name);

}
