package com.softuni.project.web;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.model.UserRole;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.controller.ExerciseController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static com.softuni.project.web.TestBuilder.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerApiTest {

    @MockitoBean
    private ExerciseService exerciseService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getExercises_shouldReturnExercisesPageWithApprovedExercises() throws Exception {
        when(exerciseService.findAllApprovedExercises()).thenReturn(List.of(randomExercise()));

        AuthenticationMetadata authenticationMetadata = userMetadata();
        User user = aRandomUser();
        user.setId(authenticationMetadata.getId());

        when(userService.getById(authenticationMetadata.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/exercises")
                .with(user(authenticationMetadata));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/exercises"))
                .andExpect(model().attributeExists("exercises"));
    }

    @Test
    void getExerciseById_shouldReturnExercisePageWithSelectedExercise() throws Exception {
        AuthenticationMetadata authenticationMetadata = userMetadata();
        User user = aRandomUser();
        user.setId(authenticationMetadata.getId());

        when(userService.getById(authenticationMetadata.getId())).thenReturn(user);

        UUID exerciseId = UUID.randomUUID();
        Exercise mockExercise = randomExercise();
        when(exerciseService.findAllApprovedExercises()).thenReturn(List.of(mockExercise));
        when(exerciseService.getById(exerciseId)).thenReturn(mockExercise);

        MockHttpServletRequestBuilder request = get("/exercises/{id}", exerciseId)
                .with(user(authenticationMetadata));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/exercises"))
                .andExpect(model().attributeExists("selectedExercise"))
                .andExpect(model().attributeExists("exercises"))
                .andExpect(model().attributeExists("user"));
    }


    @Test
    void getSubmitExerciseForm_shouldReturnSubmitExercisePage() throws Exception {
        AuthenticationMetadata authenticationMetadata = userMetadata();
        User user = aRandomUser();
        user.setId(authenticationMetadata.getId());

        when(userService.getById(authenticationMetadata.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/exercises/new")
                .with(user(authenticationMetadata));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/submit-exercise"))
                .andExpect(model().attributeExists("submitExerciseRequest"));
    }

    @Test
    void getUserExercises_shouldReturnUserExercisesPage() throws Exception {
        AuthenticationMetadata authenticationMetadata = userMetadata();
        User user = aRandomUser();
        user.setId(authenticationMetadata.getId());

        when(userService.getById(authenticationMetadata.getId())).thenReturn(user);

        when(exerciseService.findAllApprovedExercisesByUserId(user.getId())).thenReturn(List.of(new Exercise()));
        when(exerciseService.findAllPendingExercisesByUserId(user.getId())).thenReturn(List.of());
        when(exerciseService.findAllRejectedExercisesByUserId(user.getId())).thenReturn(List.of());

        MockHttpServletRequestBuilder request = get("/exercises/personal")
                .with(user(authenticationMetadata));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/your-exercises"))
                .andExpect(model().attributeExists("approvedExercises"))
                .andExpect(model().attributeExists("pendingExercises"))
                .andExpect(model().attributeExists("rejectedExercises"));
    }

    @Test
    void postSubmitExercise_withValidData_shouldRedirectToHome() throws Exception {

        MockHttpServletRequestBuilder request = post("/exercises")
                .formField("name", "Push-ups")
                .formField("description", "Do push-ups for 30 seconds")
                .formField("difficulty", "MEDIUM")
                .formField("muscleGroups", "CHEST")
                .with(user(userMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(exerciseService, times(1)).submitExercise(any(), any());
    }

    @Test
    void postSubmitExercise_withInvalidData_shouldReturnSubmitExercisePage() throws Exception {
        AuthenticationMetadata authenticationMetadata = userMetadata();
        User user = aRandomUser();
        user.setId(authenticationMetadata.getId());

        when(userService.getById(authenticationMetadata.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = post("/exercises")
                .formField("name", "")
                .formField("description", "Do push-ups for 30 seconds")
                .formField("difficulty", "MEDIUM")
                .formField("muscleGroups", "CHEST")
                .with(user(authenticationMetadata))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/submit-exercise"));
    }
}
