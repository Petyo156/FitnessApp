package com.softuni.project.web.dto;

import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.Level;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class RegisterRequest {
    @Size(min = 3, message = "Username must be at least 3 symbols.")
    private String username;

    @Size(min = 3, message = "Password must be at least 3 symbols.")
    private String password;

    @Email(message = "Invalid email address.")
    @NotNull
    private String email;

    @NotNull
    private Country country;

    @NotNull
    private Level level;
}
