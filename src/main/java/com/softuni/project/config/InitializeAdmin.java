package com.softuni.project.config;

import com.softuni.project.user.service.AdminService;
import com.softuni.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class InitializeAdmin implements CommandLineRunner {
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public InitializeAdmin(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.userCountMoreThanZero()){
            return;
        }

        adminService.insertAdmin();
    }
}
