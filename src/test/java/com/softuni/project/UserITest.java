package com.softuni.project;

import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.Level;
import com.softuni.project.user.repository.UserRepository;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class UserITest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser_happyPath() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testov")
                .password("testov")
                .level(Level.EXPERT)
                .email("testov@gmail.com")
                .country(Country.USA)
                .build();

        userService.register(registerRequest);
        assertTrue(userRepository.count() > 0);
    }
}
