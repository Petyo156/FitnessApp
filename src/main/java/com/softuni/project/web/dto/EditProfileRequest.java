package com.softuni.project.web.dto;

import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.Level;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class EditProfileRequest {
    @URL(message = "Invalid URL format")
    private String profilePicture;

    private String firstName;

    private String lastName;

    private String bio;

    private Country country;

    private Level level;
}
