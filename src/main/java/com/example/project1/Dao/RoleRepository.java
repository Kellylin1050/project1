package com.example.project1.Dao;

import com.example.project1.Entity.Role;
import com.example.project1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT r FROM Role r ")
    List<Role> getAllRoles();


    Role findByName(String name);

}
