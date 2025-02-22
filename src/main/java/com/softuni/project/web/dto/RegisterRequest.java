package com.softuni.project.web.dto;

import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.Level;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
//    @Size(min = 6, message = "Username must be at least 6 symbols.")
    private String username;

//    @Size(min = 6, message = "Password must be at least 6 symbols.")
    private String password;

//    @Email(message = "Invalid email address.")
    @NotNull
    private String email;

    @NotNull
    private Country country;

    @NotNull
    private Level level;
}
