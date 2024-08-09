package com.example.project1.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "使用者重設密碼要求")
public class UserResetPasswordRequest {
    @Schema(description = "使用者id", example = "2")
    private Integer id;
    @Schema(description = "密碼", example = "289dfjls;a")
    private String password;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

}
