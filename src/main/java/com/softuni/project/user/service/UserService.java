package com.softuni.project.user.service;

import com.softuni.project.exception.DomainException;
import com.softuni.project.user.model.User;
import com.softuni.project.user.properties.UserProperties;
import com.softuni.project.user.repository.UserRepository;
import com.softuni.project.web.dto.LoginRequest;
import com.softuni.project.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final UserProperties userProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserProperties userProperties, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userProperties = userProperties;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest registerRequest) {
        log.info("Registering user...");

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            log.info("Failed to register user.");
            throw new DomainException("User with this username already exists");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.info("Failed to register user.");
            throw new DomainException("User with this email address already exists");
        }

        User user = initializeUser(registerRequest);

        log.info("Registered user: {}", user.getUsername());
        return userRepository.save(user);
    }

    public User login(LoginRequest loginRequest) {
        log.info("Logging user...");

        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            log.info("Failed to login user.");
            throw new DomainException("User with this username does not exist");
        }

        User user = userOptional.get();
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.info("Failed to login user.");
            throw new DomainException("Wrong password");
        }

        log.info("Logged in user: {}", user.getUsername());
        return user;
    }

    private User initializeUser(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .country(registerRequest.getCountry())
                .email(registerRequest.getEmail())
                .userRole(userProperties.getUserDefaultRole())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .isActive(userProperties.isDefaultAccountState())
                .level(registerRequest.getLevel())
                .points(userProperties.getPoints().longValue())
                .build();
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
