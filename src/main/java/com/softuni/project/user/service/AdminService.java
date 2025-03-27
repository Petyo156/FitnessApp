package com.softuni.project.user.service;

import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.Level;
import com.softuni.project.user.model.User;
import com.softuni.project.user.model.UserRole;
import com.softuni.project.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public void insertAdmin() {
        User admin = initializeAdmin();
        userRepository.save(admin);

        log.info("Inserted admin successfully");
    }

    public User getAdmin() {
        return userService.getByUsername("admin");
    }

    private User initializeAdmin() {
        return User.builder()
                .userRole(UserRole.ADMIN)
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@adminov.com")
                .level(Level.PREFER_NOT_TO_SAY)
                .country(Country.USA)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }
}
