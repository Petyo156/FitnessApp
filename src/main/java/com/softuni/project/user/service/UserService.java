package com.softuni.project.user.service;

import com.softuni.project.exception.ExceptionMessages;
import com.softuni.project.exception.UserAlreadyExistsException;
import com.softuni.project.exception.UserIdDoesntExistException;
import com.softuni.project.exception.UserUsernameDoesntExistException;
import com.softuni.project.program.model.Program;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.model.UserRole;
import com.softuni.project.user.properties.UserProperties;
import com.softuni.project.user.repository.UserRepository;
import com.softuni.project.web.dto.EditProfileRequest;
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
    public User register(RegisterRequest registerRequest) {
        log.info("Attempting to register user: {}", registerRequest.getUsername());

        checkIfUsernameAlreadyExists(registerRequest);
        checkIfEmailAlreadyExists(registerRequest);

        User user = initializeUser(registerRequest);
        User save = userRepository.save(user);

        log.info("Successfully registered user: {}", user.getUsername());
        return save;
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> {

            log.error("User with ID '{}' does not exist", userId);
            return new UserIdDoesntExistException(ExceptionMessages.USER_ID_DOESNT_EXIST);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username...");
        User user = getByUsernameForLogin(username);

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getUserRole(), user.isActive());
    }

    public List<User> getAllUsersExceptGiven(UUID userId) {
        log.info("Fetching all users except for logged user");

        return userRepository.findAllByIdIsNot(userId);
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users");

        return userRepository.findAll();
    }

    public boolean userCountMoreThanZero() {
        boolean hasUsers = userRepository.count() > 0;

        log.info("Checking if user count is greater than zero: {}", hasUsers);
        return hasUsers;
    }

    public void updateUserStatus(UUID id) {
        User user = getById(id);

        boolean newStatus = !user.isActive();
        user.setActive(newStatus);
        userRepository.save(user);

        log.info("{}'s activity status changed to {}", user.getUsername(), newStatus ? "Active" : "Inactive");
    }

    public User getByUsername(String username) {
        log.info("Fetching user by username: {}", username);

        return userRepository.findByUsername(username).orElseThrow(() -> {

            log.error("User with username '{}' does not exist", username);

            return new UserUsernameDoesntExistException(ExceptionMessages.USER_USERNAME_DOESNT_EXIST);
        });
    }

    public User getByUsernameForLogin(String username) {
        log.info("Logging in user with username '{}'", username);

        return userRepository.findByUsername(username).orElseThrow(() -> {

            log.error("User with username '{}' does not exist", username);

            return new UsernameNotFoundException(ExceptionMessages.USER_USERNAME_DOESNT_EXIST);
        });
    }

    public void updateUserRole(UUID id) {
        User user = getById(id);

        if (user.getUserRole() == UserRole.ADMIN) {
            user.setUserRole(UserRole.USER);
        } else {
            user.setUserRole(UserRole.ADMIN);
        }

        userRepository.save(user);
        log.info("User with ID {} 's role was changed successfully", id);
    }

    public void editUserProfile(User user, EditProfileRequest editProfileRequest) {
        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setProfilePicture(editProfileRequest.getProfilePicture());
        user.setCountry(editProfileRequest.getCountry());
        user.setBio(editProfileRequest.getBio());

        userRepository.save(user);
        log.info("Overwritten logged user's personal information");
    }

    public void setActiveProgramForUser(User user, Program program) {
        user.setActiveProgram(program);

        userRepository.save(user);
        log.info("Set new active program for logged user");
    }

    public Program getActiveProgramForUser(User user) {
        if(null == user.getActiveProgram()) {
            log.info("Active program not found for logged user");
            return null;
        }

        return user.getActiveProgram();
    }

    public void deactivateProgramForUser(User user) {
        user.setActiveProgram(null);

        userRepository.save(user);
        log.info("Removed active program for logged user");
    }

    private void checkIfEmailAlreadyExists(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Registration failed for user '{}': Email already exists", registerRequest.getEmail());
            throw new UserAlreadyExistsException(ExceptionMessages.USER_ALREADY_EXISTS);
        }
    }

    private void checkIfUsernameAlreadyExists(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            log.warn("Registration failed for user '{}': Username already exists", registerRequest.getUsername());
            throw new UserAlreadyExistsException(ExceptionMessages.USER_ALREADY_EXISTS);
        }
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
}