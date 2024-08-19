package com.example.project1.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "使用者登入要求")
public class UserLoginRequest {
    @Schema(description = "使用者姓名", example = "admin")
    @NotBlank
    @Email
    private String username;

    @Schema(description = "密碼", example = "admin")
    @NotBlank
    private String password;

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
