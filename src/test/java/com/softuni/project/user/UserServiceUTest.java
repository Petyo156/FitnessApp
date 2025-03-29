package com.softuni.project.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.softuni.project.exception.UserAlreadyExistsException;
import com.softuni.project.exception.UserIdDoesntExistException;
import com.softuni.project.exception.UserUsernameDoesntExistException;
import com.softuni.project.program.model.Program;
import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.User;
import com.softuni.project.user.model.UserRole;
import com.softuni.project.user.properties.UserProperties;
import com.softuni.project.user.repository.UserRepository;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.EditProfileRequest;
import com.softuni.project.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUTest {

    @Mock
    private UserProperties userProperties;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_shouldSaveUser_whenValidRequest() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userProperties.getUserDefaultRole()).thenReturn(UserRole.USER);
        when(userProperties.isDefaultAccountState()).thenReturn(true);

        userService.register(registerRequest);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(registerRequest));
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(registerRequest));
    }

    @Test
    void getById_shouldReturnUser_whenUserExists() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getById(userId);

        assertEquals(user, result);
    }

    @Test
    void getById_shouldThrowException_whenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserIdDoesntExistException.class, () -> userService.getById(userId));
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setUserRole(UserRole.USER);
        user.setActive(true);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertEquals("testUser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistentUser"));
    }

    @Test
    void getAllUsersExceptGiven_shouldReturnUsersExcludingGivenUser() {
        UUID userId = UUID.randomUUID();
        List<User> users = List.of(new User(), new User());

        when(userRepository.findAllByIdIsNot(userId)).thenReturn(users);

        List<User> result = userService.getAllUsersExceptGiven(userId);

        assertEquals(2, result.size());
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void userCountMoreThanZero_shouldReturnTrue_whenUsersExist() {
        when(userRepository.count()).thenReturn(5L);

        assertTrue(userService.userCountMoreThanZero());
    }

    @Test
    void userCountMoreThanZero_shouldReturnFalse_whenNoUsersExist() {
        when(userRepository.count()).thenReturn(0L);

        assertFalse(userService.userCountMoreThanZero());
    }

    @Test
    void updateUserStatus_shouldToggleUserStatus() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.updateUserStatus(String.valueOf(userId));

        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserRole_shouldToggleBetweenAdminAndUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserRole(UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.updateUserRole(String.valueOf(userId));

        assertEquals(UserRole.ADMIN, user.getUserRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void editUserProfile_shouldUpdateUserDetails() {
        User user = new User();
        EditProfileRequest request = new EditProfileRequest();
        request.setFirstName("NewFirstName");
        request.setLastName("NewLastName");
        request.setProfilePicture("newPic.jpg");
        request.setCountry(Country.USA);
        request.setBio("NewBio");

        userService.editUserProfile(user, request);

        assertEquals("NewFirstName", user.getFirstName());
        assertEquals("NewLastName", user.getLastName());
        assertEquals("newPic.jpg", user.getProfilePicture());
        assertEquals(Country.USA, user.getCountry());
        assertEquals("NewBio", user.getBio());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void setActiveProgramForUser_shouldUpdateActiveProgram() {
        User user = new User();
        Program program = new Program();

        userService.setActiveProgramForUser(user, program);

        assertEquals(program, user.getActiveProgram());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getActiveProgramForUser_shouldReturnProgram_whenSet() {
        Program program = new Program();
        User user = new User();
        user.setActiveProgram(program);

        assertEquals(program, userService.getActiveProgramForUser(user));
    }

    @Test
    void getActiveProgramForUser_shouldReturnNull_whenNotSet() {
        User user = new User();

        assertNull(userService.getActiveProgramForUser(user));
    }

    @Test
    void deactivateProgramForUser_shouldRemoveActiveProgram() {
        User user = new User();
        user.setActiveProgram(new Program());

        userService.deactivateProgramForUser(user);

        assertNull(user.getActiveProgram());
        verify(userRepository, times(1)).save(user);
    }
}
