package com.softuni.project.web;

import com.softuni.project.common.DayOfWeek;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.controller.WorkoutController;
import com.softuni.project.web.dto.SubmitWorkoutRequest;
import com.softuni.project.web.dto.ViewWorkoutResponse;
import com.softuni.project.web.dto.WorkoutExerciseEntry;
import com.softuni.project.workout.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static com.softuni.project.web.TestBuilder.aRandomUser;
import static com.softuni.project.web.TestBuilder.userMetadata;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutController.class)
public class WorkoutControllerApiTest {

    @MockitoBean
    private WorkoutService workoutService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ExerciseService exerciseService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getYourWorkouts_shouldReturnYourWorkoutsPage() throws Exception {
        User user = aRandomUser();
        WorkoutExerciseEntry workoutExerciseEntry = WorkoutExerciseEntry.builder()
                .exerciseName("Push up")
                .reps(2)
                .addedWeight(2.0)
                .sets(2)
                .build();

        ViewWorkoutResponse viewWorkoutResponse = ViewWorkoutResponse.builder()
                .workoutId(UUID.randomUUID().toString())
                .approximateDuration(60)
                .dayOfWeek(DayOfWeek.MONDAY)
                .exercises(List.of(workoutExerciseEntry))
                .build();

        List<ViewWorkoutResponse> workouts = List.of(viewWorkoutResponse);

        when(userService.getById(UUID.randomUUID())).thenReturn(user);
        when(workoutService.getWorkoutsForUser(any())).thenReturn(workouts);

        MockHttpServletRequestBuilder request = get("/workouts/personal")
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/your-workouts"))
                .andExpect(model().attributeExists("workouts"));

        verify(userService, times(1)).getById(UUID.randomUUID());
        verify(workoutService, times(1)).getWorkoutsForUser(any());
    }

    @Test
    void getCreateWorkoutPage_shouldReturnCreateWorkoutPage() throws Exception {
        List<String> allExercises = List.of("Push Up", "Squat", "Lunge");

        when(exerciseService.findAllApprovedExercisesNames()).thenReturn(allExercises);

        MockHttpServletRequestBuilder request = get("/workouts/new")
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/submit-workout"))
                .andExpect(model().attributeExists("submitWorkoutRequest"))
                .andExpect(model().attributeExists("allExercises"));

        verify(exerciseService, times(1)).findAllApprovedExercisesNames();
    }

    @Test
    void postCreateWorkout_shouldRedirectToHome() throws Exception {
        User user = aRandomUser();
        SubmitWorkoutRequest submitWorkoutRequest = new SubmitWorkoutRequest();
        WorkoutExerciseEntry workoutExerciseEntry = WorkoutExerciseEntry.builder()
                .exerciseName("Push up")
                .reps(2)
                .addedWeight(2.0)
                .sets(2)
                .build();
        submitWorkoutRequest.setExercises(List.of(workoutExerciseEntry));
        submitWorkoutRequest.setApproximateDuration(60);
        submitWorkoutRequest.setAdditionalInfo("Top workout");

        when(userService.getById(UUID.randomUUID())).thenReturn(user);

        MockHttpServletRequestBuilder request = post("/workouts")
                .with(user(userMetadata()))
                .with(csrf())
                .flashAttr("submitWorkoutRequest", submitWorkoutRequest);

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(workoutService, times(1)).submitWorkout(any(), any());
    }

    @Test
    void postCreateWorkout_withValidationErrors_shouldReturnCreateWorkoutPage() throws Exception {
        SubmitWorkoutRequest submitWorkoutRequest = new SubmitWorkoutRequest();

        MockHttpServletRequestBuilder request = post("/workouts")
                .with(user(userMetadata()))
                .with(csrf())
                .flashAttr("submitWorkoutRequest", submitWorkoutRequest);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/submit-workout"));

        verify(workoutService, times(0)).submitWorkout(any(), any());
    }
}
