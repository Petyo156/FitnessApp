package com.softuni.project.user.service;

import com.softuni.project.exception.DomainException;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.properties.UserProperties;
import com.softuni.project.user.repository.UserRepository;
import com.softuni.project.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements UserDetailsService {
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
    public void register(RegisterRequest registerRequest) {
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
        userRepository.save(user);
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
                .build();
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new DomainException("User with this id does not exist"));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with this username does not exist."));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getUserRole(), user.isActive());
    }


    public List<User> getAllUsersExceptGiven(UUID userId) {
        return userRepository.findAllByIdIsNot(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean userCountMoreThanZero() {
        return userRepository.count() > 0;
    }

    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    public void changeUserStatus(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DomainException("User with this id does not exist"));

        user.setActive(!user.isActive());

        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with this username does not exist"));
    }
}
