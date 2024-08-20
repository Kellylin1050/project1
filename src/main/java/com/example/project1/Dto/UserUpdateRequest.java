package com.example.project1.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import org.springframework.security.core.userdetails.User;
@Schema(description = "使用者更新要求")
public class UserUpdateRequest {
    @Schema(description = "使用者id", example = "3")
    private Integer id;
    @Schema(description = "姓名", example = "Diana")
    private String name;
    @Schema(description = "電話", example = "0947382613")
    private String phone;
    //@Schema(description = "使用者姓名", example = "Diana_liu")
    //private String username;


    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPhone(){return phone;}
    public void setPhone(String phone){this.phone = phone;}
    //public String getUsername(){return username;}
    //public void setUsername(String username){this.username = username;}


}
