package com.softuni.project.config;

import com.softuni.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class InitializeBaseUsers implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public InitializeBaseUsers(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userService.onlyAdminIsAdded()){
            return;
        }

        userService.insertFirstBaseUser();
        userService.insertSecondBaseUser();
    }
}
