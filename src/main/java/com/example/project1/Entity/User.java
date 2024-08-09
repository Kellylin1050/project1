package com.example.project1.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.*;

@Schema(description = "User實體")
@Entity
@Table(name = "user")
public class User {
    @Schema(description = "使用者id", example = "1")
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Schema(description = "使用者名稱", example = "William_chou")
    @Column(nullable = false,unique = true)
    private String username;
    @Schema(description = "密碼", example = "4730jihfuiw")
    @Column(nullable = false)
    private String password;
    @Schema(description = "姓名", example = "William")
    private String name;
    @Schema(description = "書名", example = "0947382645")
    private String phone;

    @Schema(description = "信箱", example = "William38201@gmail.com")
    private String email;

    private boolean enabled;

    @Schema(description = "User的角色集合")
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    //private Collection<Role> roles;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }
}

