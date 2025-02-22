package com.softuni.project.user.properties;

import com.softuni.project.user.model.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "users")
public class UserProperties {

    private UserRole userDefaultRole;
    private boolean defaultAccountState;
    private Integer points;

}
