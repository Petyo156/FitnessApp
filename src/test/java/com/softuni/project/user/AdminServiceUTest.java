package com.softuni.project.user;

import static org.mockito.Mockito.*;

import com.softuni.project.user.model.UserRole;
import com.softuni.project.user.model.Level;
import com.softuni.project.user.model.Country;
import com.softuni.project.user.repository.UserRepository;
import com.softuni.project.user.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @Test
    void insertAdmin_shouldCreateAndSaveAdmin() {
        when(passwordEncoder.encode("admin")).thenReturn("encodedPassword");

        adminService.insertAdmin();

        verify(userRepository, times(1)).save(argThat(admin ->
                admin.getUserRole() == UserRole.ADMIN &&
                        admin.getUsername().equals("admin") &&
                        admin.getPassword().equals("encodedPassword") &&
                        admin.getEmail().equals("admin@adminov.com") &&
                        admin.getLevel() == Level.PREFER_NOT_TO_SAY &&
                        admin.getCountry() == Country.USA &&
                        admin.isActive() &&
                        admin.getCreatedOn() != null &&
                        admin.getUpdatedOn() != null
        ));
    }
}