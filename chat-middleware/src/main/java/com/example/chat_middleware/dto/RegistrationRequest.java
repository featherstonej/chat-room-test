package com.example.chat_middleware.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class RegistrationRequest {

    @NotBlank @Size(min = 3, max = 32)
    @Pattern (regexp = "^[a-zA-Z0-9._-]{3,32}$")
    private String username;

    @NotBlank @Size(min = 8, max = 72)
    private String password;

    public RegistrationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

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
