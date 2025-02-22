package com.softuni.project.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

//    @Size(min = 6, message = "Username must be at least 6 symbols.")
    private String username;

//    @Size(min = 6, message = "Password must be at least 6 symbols.")
    private String password;

    @Override
    public String toString() {
        return super.toString();
    }
}
