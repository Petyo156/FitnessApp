package com.softuni.project.web.dto;

import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.Level;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class EditProfileRequest {
    @URL(message = "Invalid URL format")
    private String profilePicture;

    @Size(max = 50, message = "First name has to be maximum 50 symbols")
    private String firstName;

    @Size(max = 50, message = "Last name has to be maximum 50 symbols")
    private String lastName;

    @Size(max = 50, message = "Bio has to be maximum 50 symbols")
    private String bio;

    @NotNull
    private Country country;

    @NotNull
    private Level level;
}
