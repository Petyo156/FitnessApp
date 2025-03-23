package com.softuni.project.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.model.ExerciseStatus;
import com.softuni.project.excersise.model.MuscleGroup;
import com.softuni.project.program.model.Difficulty;
import com.softuni.project.program.model.Program;
import com.softuni.project.user.model.User;
import com.softuni.project.web.dto.*;
import com.softuni.project.workout.model.Workout;
import com.softuni.project.workoutexercise.model.WorkoutExercise;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

class MapperUTest {

    private final ProgramFormRequestToProgramMapper programMapper = new ProgramFormRequestToProgramMapper();
    private final ProgramToViewProgramResponseMapper programToViewMapper = new ProgramToViewProgramResponseMapper();
    private final SubmitExerciseRequestToExerciseMapper submitExerciseMapper = new SubmitExerciseRequestToExerciseMapper();
    private final SubmitWorkoutRequestToWorkoutMapper submitWorkoutMapper = new SubmitWorkoutRequestToWorkoutMapper();
    private final WorkoutExerciseToWorkoutExerciseEntryMapper workoutExerciseEntryMapper = new WorkoutExerciseToWorkoutExerciseEntryMapper();
    private final WorkoutToViewWorkoutResponseMapper workoutToViewMapper = new WorkoutToViewWorkoutResponseMapper();

    @Test
    void programFormRequestToProgramMapper_shouldReturnProgram() {
        ProgramFormRequest programFormRequest = new ProgramFormRequest();
        programFormRequest.setName("Strength Training");
        programFormRequest.setDescription("A program for strength training.");
        programFormRequest.setDifficulty(Difficulty.HARD);
        programFormRequest.setShared(true);

        Program result = programMapper.map(programFormRequest);

        assertNotNull(result);
        assertEquals("Strength Training", result.getName());
        assertEquals("A program for strength training.", result.getDescription());
        assertEquals(Difficulty.HARD, result.getDifficulty());
        assertTrue(result.getSharedWithOthers());
        assertNotNull(result.getCreatedOn());
    }

    @Test
    void programToViewProgramResponseMapper_shouldReturnViewProgramResponse() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Program program = Program.builder().id(UUID.randomUUID()).build();
        program.setId(UUID.randomUUID());
        program.setName("Strength Training");
        program.setDescription("A program for strength training.");
        program.setDifficulty(Difficulty.HARD);
        program.setCreatedOn(LocalDateTime.now());
        program.setSharedWithOthers(true);
        program.setUser(user);

        ViewProgramResponse result = programToViewMapper.map(program);

        assertNotNull(result);
        assertEquals("Strength Training", result.getName());
        assertEquals("A program for strength training.", result.getDescription());
        assertEquals(Difficulty.HARD, result.getDifficulty());
        assertNotNull(result.getCreatedOn());
        assertTrue(result.getSharedWithOthers());
        assertNotNull(result.getId());
    }

    @Test
    void submitExerciseRequestToExerciseMapper_shouldReturnExercise() {
        SubmitExerciseRequest request = new SubmitExerciseRequest();
        request.setName("Push-up");
        request.setDescription("A basic upper body exercise");
        request.setMuscleGroups(List.of(MuscleGroup.CHEST));
        request.setMediaUrl("http://example.com/pushup");
        request.setDifficulty(com.softuni.project.excersise.model.Difficulty.MEDIUM);

        Exercise result = submitExerciseMapper.map(request);

        assertNotNull(result);
        assertEquals("Push-up", result.getName());
        assertEquals("A basic upper body exercise", result.getDescription());
        assertEquals(List.of(MuscleGroup.CHEST), result.getMuscleGroups());
        assertEquals("http://example.com/pushup", result.getMediaUrl());
        assertEquals(com.softuni.project.excersise.model.Difficulty.MEDIUM, result.getDifficulty());
        assertNotNull(result.getCreatedOn());
        assertEquals(ExerciseStatus.PENDING, result.getStatus());
    }

    @Test
    void submitWorkoutRequestToWorkoutMapper_shouldReturnWorkout() {
        SubmitWorkoutRequest request = new SubmitWorkoutRequest();
        request.setAdditionalInfo("Warm-up and strength training.");
        request.setApproximateDuration(60);

        Workout result = submitWorkoutMapper.map(request);

        assertNotNull(result);
        assertEquals("Warm-up and strength training.", result.getAdditionalInfo());
        assertEquals(60, result.getDuration());
    }

    @Test
    void workoutExerciseToWorkoutExerciseEntryMapper_shouldReturnWorkoutExerciseEntry() {
        Exercise exercise = Exercise.builder().id(UUID.randomUUID()).build();
        WorkoutExercise workoutExercise = WorkoutExercise.builder().id(UUID.randomUUID()).build();;
        workoutExercise.setExercise(exercise);
        workoutExercise.setSets(3);
        workoutExercise.setReps(10);
        workoutExercise.setAddedWeight(50.0);

        WorkoutExerciseEntry result = workoutExerciseEntryMapper.map(workoutExercise);

        assertNotNull(result);
        assertEquals(3, result.getSets());
        assertEquals(10, result.getReps());
        assertEquals(50.0, result.getAddedWeight());
        assertNotNull(result.getExerciseId());
    }

    @Test
    void workoutToViewWorkoutResponseMapper_shouldReturnViewWorkoutResponse() {
        Workout workout = new Workout();
        workout.setId(UUID.randomUUID());
        workout.setAdditionalInfo("Strength training session");
        workout.setDuration(45);

        ViewWorkoutResponse result = workoutToViewMapper.map(workout);

        assertNotNull(result);
        assertEquals("Strength training session", result.getAdditionalInfo());
        assertEquals(45, result.getApproximateDuration());
        assertNotNull(result.getWorkoutId());
    }
}
