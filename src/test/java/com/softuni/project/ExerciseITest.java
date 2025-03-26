package com.softuni.project;

import com.softuni.project.excersise.model.Difficulty;
import com.softuni.project.excersise.model.MuscleGroup;
import com.softuni.project.excersise.repository.ExerciseRepository;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.Level;
import com.softuni.project.user.model.User;
import com.softuni.project.user.model.UserRole;
import com.softuni.project.user.repository.UserRepository;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.RegisterRequest;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class ExerciseITest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    void submitExercise_happyPath() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("testov")
                .password("testov")
                .level(Level.EXPERT)
                .email("testov@gmail.com")
                .country(Country.USA)
                .build();

        User user = userService.register(registerRequest);
        assertTrue(userRepository.count() > 0);

        SubmitExerciseRequest submitExerciseRequest = SubmitExerciseRequest.builder()
                .name("the best")
                .muscleGroups(List.of(MuscleGroup.CHEST))
                .description("the best")
                .difficulty(Difficulty.HARD)
                .build();

        AuthenticationMetadata authenticationMetadata = AuthenticationMetadata.builder()
                .role(UserRole.USER)
                .isActive(true)
                .username(user.getUsername())
                .password(user.getPassword())
                .id(user.getId())
                .build();

        exerciseService.submitExercise(submitExerciseRequest, authenticationMetadata);
        assertTrue(exerciseRepository.count() > 0);
    }
}
