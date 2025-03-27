package com.softuni.project;

import com.softuni.project.excersise.model.Difficulty;
import com.softuni.project.excersise.model.Exercise;
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
import com.softuni.project.web.dto.*;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workout.repository.WorkoutRepository;
import com.softuni.project.workout.service.WorkoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class WorkoutITest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    public void submitWorkout_happyPath() {
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

        Exercise exercise = exerciseService.submitExercise(submitExerciseRequest, user);
        assertTrue(exerciseRepository.count() > 0);

        WorkoutExerciseEntry entry = WorkoutExerciseEntry.builder()
                .exerciseId(exercise.getId().toString())
                .exerciseName(exercise.getName())
                .sets(2)
                .reps(2)
                .addedWeight(2.0)
                .build();

        SubmitWorkoutRequest submitWorkoutRequest = new SubmitWorkoutRequest();
        submitWorkoutRequest.setAdditionalInfo("additional info");
        submitWorkoutRequest.setApproximateDuration(60);
        submitWorkoutRequest.setExercises(List.of(entry));

        workoutService.submitWorkout(submitWorkoutRequest, user);
        List<Workout> workouts = workoutRepository.findAllByAddedBy(user);
        assertFalse(workouts.isEmpty());

        List<ViewWorkoutResponse> responses = workoutService.getWorkoutsForUser(user);
        assertThat(responses, hasSize(1));
    }
}
