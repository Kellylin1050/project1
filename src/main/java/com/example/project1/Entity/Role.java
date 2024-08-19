package com.example.project1.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Schema(description = "角色實體")
@Entity
@Table(name = "role")
public class Role {
    @Schema(description = "角色id", example = "1")
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Schema(description = "角色名稱", example = "ADMIN")
    private String name;

   @Schema(description = "與此角色相關聯的User")
   @ManyToMany(mappedBy = "roles",cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
   private Set<User> users = new HashSet<>();


    public Role(){}
    public Role(String name){
        this.name = name;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
