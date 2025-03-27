package com.softuni.project.web;

import com.softuni.project.common.DayOfWeek;
import com.softuni.project.excersise.model.Difficulty;
import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.model.ExerciseStatus;
import com.softuni.project.excersise.model.MuscleGroup;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.Country;
import com.softuni.project.user.model.Level;
import com.softuni.project.user.model.User;
import com.softuni.project.user.model.UserRole;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workoutschedule.model.WorkoutSchedule;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TestBuilder {
    public static User aRandomUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("User")
                .password("123123")
                .userRole(UserRole.USER)
                .country(Country.USA)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .level(Level.EXPERT)
                .build();
    }

    public static AuthenticationMetadata adminMetadata() {
        return AuthenticationMetadata.builder()
                .id(UUID.randomUUID())
                .username("User123")
                .password("123123")
                .role(UserRole.ADMIN)
                .isActive(true)
                .build();
    }

    public static AuthenticationMetadata userMetadata() {
        return AuthenticationMetadata.builder()
                .id(UUID.randomUUID())
                .username("User123")
                .password("123123")
                .role(UserRole.USER)
                .isActive(true)
                .build();
    }

    public static Exercise randomExercise() {
        return Exercise.builder()
                .id(UUID.randomUUID())
                .createdBy(aRandomUser())
                .createdOn(LocalDateTime.now())
                .status(ExerciseStatus.APPROVED)
                .muscleGroups(List.of(MuscleGroup.LOWER_BACK))
                .name("Pull up")
                .approvedBy(aRandomUser())
                .description("Top exercise")
                .difficulty(Difficulty.HARD)
                .build();
    }

    public static WorkoutSchedule randomWorkoutSchedule() {
        return WorkoutSchedule.builder()
                .id(UUID.randomUUID())
                .dayOfWeek(DayOfWeek.MONDAY)
                .workout(new Workout())
                .build();
    }

    public static Workout randomWorkout() {
        return Workout.builder()
                .id(UUID.randomUUID())
                .addedBy(aRandomUser())
                .duration(60)
                .additionalInfo("Top workout")
                .additionalInfo("More information")
                .workoutSchedules(List.of(randomWorkoutSchedule()))
                .build();
    }

}
