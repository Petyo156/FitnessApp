package com.softuni.project.web;

import com.softuni.project.common.DayOfWeek;
import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.log.service.LogService;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.controller.LogController;
import com.softuni.project.web.dto.*;
import com.softuni.project.workout.service.WorkoutService;
import com.softuni.project.workout.model.Workout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.softuni.project.web.TestBuilder.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogController.class)
public class LogControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WorkoutService workoutService;

    @MockitoBean
    private LogService logService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postCreateWorkoutLog_shouldRedirectToHome() throws Exception {

        Exercise exercise = randomExercise();
        List<LogExerciseResponse> responses = new ArrayList<>();
        LogExerciseResponse response = LogExerciseResponse.builder()
                .exerciseId(exercise.getId().toString())
                .sets(2)
                .reps(2)
                .addedWeight(0.0)
                .build();
        responses.add(response);

        WorkoutLogRequest workoutLogRequest = new WorkoutLogRequest();
        workoutLogRequest.setLoggedExercises(responses);

        when(userService.getById(any())).thenReturn(aRandomUser());

        Workout workout = randomWorkout();
        when(workoutService.getById(any())).thenReturn(workout);

        MockHttpServletRequestBuilder request = post("/logs/{dayOfWeek}/{workoutId}", "Monday", workout.getId())
                .with(user(userMetadata()))
                .with(csrf())
                .formField("loggedExercises[0].sets", "2")
                .formField("loggedExercises[0].reps", "2")
                .formField("loggedExercises[0].weight", "2");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(logService, times(1)).createLog(any(), any(), any());
    }

    @Test
    void getShowLogWorkoutPage_shouldReturnLogWorkoutPage() throws Exception {
        Workout workout = randomWorkout();

        WorkoutExerciseEntry workoutExerciseEntry = WorkoutExerciseEntry.builder()
                .exerciseName("Pushing up")
                .addedWeight(0.0)
                .sets(3)
                .reps(3)
                .build();

        ViewWorkoutResponse workoutResponse = ViewWorkoutResponse.builder()
                .workoutId(workout.getId().toString())
                .dayOfWeek(DayOfWeek.MONDAY)
                .approximateDuration(60)
                .exercises(List.of(workoutExerciseEntry))
                .additionalInfo("opa topa")
                .build();


        Exercise exercise = randomExercise();
        List<LogExerciseResponse> responses = new ArrayList<>();
        LogExerciseResponse response = LogExerciseResponse.builder()
                .exerciseId(exercise.getId().toString())
                .sets(2)
                .reps(2)
                .addedWeight(0.0)
                .build();
        responses.add(response);

        WorkoutLogRequest workoutLogRequest = new WorkoutLogRequest();
        workoutLogRequest.setLoggedExercises(responses);


        when(workoutService.getById(any())).thenReturn(workout);
        when(workoutService.getViewWorkoutResponseByWorkout(any())).thenReturn(workoutResponse);
        when(workoutService.initializeWorkoutLogRequest(any())).thenReturn(workoutLogRequest);

        MockHttpServletRequestBuilder request = get("/logs/{dayOfWeek}/{workoutId}", "Monday", workout.getId())
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/log-workout"))
                .andExpect(model().attributeExists("workoutResponse"))
                .andExpect(model().attributeExists("workoutLogRequest"))
                .andExpect(model().attributeExists("dayOfWeek"));

        verify(workoutService, times(1)).getById(any());
        verify(workoutService, times(1)).getViewWorkoutResponseByWorkout(any());
        verify(workoutService, times(1)).initializeWorkoutLogRequest(any());
    }

    @Test
    void getUserLogs_shouldReturnLogsPage() throws Exception {

        ViewLoggedExerciseResponse exerciseResponse = ViewLoggedExerciseResponse.builder()
                .exerciseName("Pushing up")
                .addedWeight(0.0)
                .completedSets(3)
                .completedReps(3)
                .build();
        ViewLogResponse viewLogResponse = new ViewLogResponse();
        viewLogResponse.setLoggedExercises(List.of(exerciseResponse));
        viewLogResponse.setLogId(UUID.randomUUID().toString());
        viewLogResponse.setCompletionDate(LocalDateTime.now());

        List<ViewLogResponse> logs = List.of(viewLogResponse);

        when(userService.getById(any())).thenReturn(aRandomUser());
        when(logService.getViewLogResponsesForUser(any())).thenReturn(logs);

        MockHttpServletRequestBuilder request = get("/logs")
                .with(user(userMetadata()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/your-logs"))
                .andExpect(model().attributeExists("logs"));

        verify(logService, times(1)).getViewLogResponsesForUser(any());
    }

    @Test
    void deleteLog_shouldRedirectToLogs() throws Exception {
        UUID logId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = delete("/logs/{id}", logId)
                .with(user(userMetadata()))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/logs"));

        verify(logService, times(1)).deleteLogById(any());
    }

}

