package com.softuni.project.exercise;

import com.softuni.project.exception.ExceptionMessages;
import com.softuni.project.exception.ExerciseAlreadyExistsException;
import com.softuni.project.exception.ExerciseDoesntExistException;
import com.softuni.project.exception.ExerciseNotApprovedException;
import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.model.ExerciseStatus;
import com.softuni.project.excersise.repository.ExerciseRepository;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.mapper.Mapper;
import com.softuni.project.user.model.User;
import com.softuni.project.user.model.UserRole;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.softuni.project.web.TestBuilder.aRandomUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceUTest {
    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserService userService;

    @Mock
    private Mapper<SubmitExerciseRequest, Exercise> exerciseMapper;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    void submitExercise_whenExerciseAlreadyExists_thenThrowExerciseAlreadyExistsException() {
        SubmitExerciseRequest submitExerciseRequest = SubmitExerciseRequest.builder().name("Push-up").build();
        User createdBy = aRandomUser();
        when(exerciseRepository.findByName(submitExerciseRequest.getName())).thenReturn(Optional.of(new Exercise()));

        assertThrows(ExerciseAlreadyExistsException.class, () -> exerciseService.submitExercise(submitExerciseRequest, createdBy));
        verify(exerciseRepository, times(1)).findByName(submitExerciseRequest.getName());
        verify(exerciseRepository, never()).save(any());
    }

    @Test
    void submitExercise_whenExerciseDoesNotExist_thenSubmitAndSaveExercise() {
        SubmitExerciseRequest submitExerciseRequest = SubmitExerciseRequest.builder().name("Push-up").build();
        User createdBy = aRandomUser();
        when(exerciseRepository.findByName(submitExerciseRequest.getName())).thenReturn(Optional.empty());
        Exercise mappedExercise = new Exercise();
        when(exerciseMapper.map(submitExerciseRequest)).thenReturn(mappedExercise);
        when(userService.getById(createdBy.getId())).thenReturn(createdBy);

        Exercise result = exerciseService.submitExercise(submitExerciseRequest, createdBy);

        assertNotNull(result);
        verify(exerciseRepository, times(1)).findByName(submitExerciseRequest.getName());
        verify(exerciseRepository, times(1)).save(any());
    }

    @Test
    void submitExercise_whenUserIsAdmin_thenSetApprovedStatus() {
        SubmitExerciseRequest submitExerciseRequest = SubmitExerciseRequest.builder().name("Push-up").build();
        User createdBy = aRandomUser();
        when(exerciseRepository.findByName(submitExerciseRequest.getName())).thenReturn(Optional.empty());
        Exercise mappedExercise = new Exercise();
        when(exerciseMapper.map(submitExerciseRequest)).thenReturn(mappedExercise);
        when(userService.getById(createdBy.getId())).thenReturn(createdBy);

        createdBy.setUserRole(UserRole.ADMIN);

        Exercise result = exerciseService.submitExercise(submitExerciseRequest, createdBy);

        assertEquals(ExerciseStatus.APPROVED, result.getStatus());
        verify(exerciseRepository, times(1)).findByName(submitExerciseRequest.getName());
        verify(exerciseRepository, times(1)).save(any());
    }

    @Test
    void submitExercise_whenUserIsNotAdmin_thenStatusIsNotApproved() {
        SubmitExerciseRequest submitExerciseRequest = SubmitExerciseRequest.builder().name("Push-up").build();
        User createdBy = aRandomUser();
        when(exerciseRepository.findByName(submitExerciseRequest.getName())).thenReturn(Optional.empty());
        Exercise mappedExercise = new Exercise();
        when(exerciseMapper.map(submitExerciseRequest)).thenReturn(mappedExercise);
        when(userService.getById(createdBy.getId())).thenReturn(createdBy);

        createdBy.setUserRole(UserRole.USER);

        Exercise result = exerciseService.submitExercise(submitExerciseRequest, createdBy);

        assertNotEquals(ExerciseStatus.APPROVED, result.getStatus());
        verify(exerciseRepository, times(1)).findByName(submitExerciseRequest.getName());
        verify(exerciseRepository, times(1)).save(any());
    }

    @Test
    void submitExercise_whenExerciseCreated_thenSetCorrectCreator() {
        SubmitExerciseRequest submitExerciseRequest = SubmitExerciseRequest.builder().name("Push-up").build();
        User createdBy = aRandomUser();

        when(exerciseRepository.findByName(submitExerciseRequest.getName())).thenReturn(Optional.empty());
        Exercise mappedExercise = new Exercise();
        when(exerciseMapper.map(submitExerciseRequest)).thenReturn(mappedExercise);
        when(userService.getById(createdBy.getId())).thenReturn(createdBy);

        Exercise result = exerciseService.submitExercise(submitExerciseRequest, createdBy);

        assertEquals(createdBy, result.getCreatedBy());
        verify(exerciseRepository, times(1)).findByName(submitExerciseRequest.getName());
        verify(exerciseRepository, times(1)).save(any());
    }


    @Test
    void submitExercise_whenExerciseAlreadyExists_thenThrowException() {
        SubmitExerciseRequest request = SubmitExerciseRequest.builder().name("Push-up").build();

        when(exerciseRepository.findByName(request.getName())).thenReturn(Optional.of(new Exercise()));
        assertThrows(ExerciseAlreadyExistsException.class, () -> exerciseService.submitExercise(request, aRandomUser()));

        verify(exerciseRepository, times(1)).findByName(request.getName());
        verify(exerciseRepository, never()).save(any());
    }

    @Test
    void getById_whenExerciseExists_thenReturnExercise() {
        UUID exerciseId = UUID.randomUUID();
        Exercise exercise = Exercise.builder().id(exerciseId).build();
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.getById(exerciseId);

        assertNotNull(result);
        verify(exerciseRepository, times(1)).findById(exerciseId);
    }

    @Test
    void getById_whenExerciseDoesNotExist_thenThrowException() {
        UUID exerciseId = UUID.randomUUID();
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        assertThrows(ExerciseDoesntExistException.class, () -> exerciseService.getById(exerciseId));

        verify(exerciseRepository, times(1)).findById(exerciseId);
    }

    @Test
    void throwIfNotApproved_whenExerciseNotApproved_thenThrowException() {
        Exercise exercise = new Exercise();
        exercise.setStatus(ExerciseStatus.PENDING);

        assertThrows(ExerciseNotApprovedException.class, () -> exerciseService.throwIfNotApproved(exercise));
    }

    @Test
    void throwIfNotApproved_whenExerciseApproved_thenDoNothing() {
        Exercise exercise = new Exercise();
        exercise.setStatus(ExerciseStatus.APPROVED);

        assertDoesNotThrow(() -> exerciseService.throwIfNotApproved(exercise));
    }

    @Test
    void approveById_whenValidId_thenApproveExercise() {
        UUID exerciseId = UUID.randomUUID();
        Exercise exercise = new Exercise();
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));

        exerciseService.approveById(exerciseId);

        assertEquals(ExerciseStatus.APPROVED, exercise.getStatus());
        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test
    void rejectById_whenValidId_thenRejectExercise() {
        UUID exerciseId = UUID.randomUUID();
        Exercise exercise = new Exercise();
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(exercise));

        exerciseService.rejectById(exerciseId);

        assertEquals(ExerciseStatus.REJECTED, exercise.getStatus());
        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test
    void findAllPendingExercises_whenCalled_thenReturnPendingExercises() {
        List<Exercise> exercises = List.of(new Exercise());
        when(exerciseRepository.findByStatus(ExerciseStatus.PENDING)).thenReturn(exercises);

        List<Exercise> result = exerciseService.findAllPendingExercises();

        assertEquals(exercises, result);
    }

    @Test
    void deleteAllRejectedExercises_whenCalled_thenDeleteRejectedExercises() {
        exerciseService.deleteAllRejectedExercises();

        verify(exerciseRepository, times(1)).deleteAllByStatus(ExerciseStatus.REJECTED);
    }

    @Test
    void findAllApprovedExercisesByUserId_whenExercisesExist_thenReturnList() {
        UUID userId = UUID.randomUUID();
        List<Exercise> exercises = List.of(new Exercise());
        when(exerciseRepository.findByStatusAndCreatedBy_Id(ExerciseStatus.APPROVED, userId)).thenReturn(exercises);

        List<Exercise> result = exerciseService.findAllApprovedExercisesByUserId(userId);

        assertEquals(exercises, result);
        verify(exerciseRepository, times(1)).findByStatusAndCreatedBy_Id(ExerciseStatus.APPROVED, userId);
    }

    @Test
    void findAllPendingExercisesByUserId_whenExercisesExist_thenReturnList() {
        UUID userId = UUID.randomUUID();
        List<Exercise> exercises = List.of(new Exercise());
        when(exerciseRepository.findByStatusAndCreatedBy_Id(ExerciseStatus.PENDING, userId)).thenReturn(exercises);

        List<Exercise> result = exerciseService.findAllPendingExercisesByUserId(userId);

        assertEquals(exercises, result);
        verify(exerciseRepository, times(1)).findByStatusAndCreatedBy_Id(ExerciseStatus.PENDING, userId);
    }

    @Test
    void findAllRejectedExercisesByUserId_whenExercisesExist_thenReturnList() {
        UUID userId = UUID.randomUUID();
        List<Exercise> exercises = List.of(new Exercise());
        when(exerciseRepository.findByStatusAndCreatedBy_Id(ExerciseStatus.REJECTED, userId)).thenReturn(exercises);

        List<Exercise> result = exerciseService.findAllRejectedExercisesByUserId(userId);

        assertEquals(exercises, result);
        verify(exerciseRepository, times(1)).findByStatusAndCreatedBy_Id(ExerciseStatus.REJECTED, userId);
    }

    @Test
    void findByName_whenExerciseExists_thenReturnExercise() {
        String exerciseName = "Push-up";
        Exercise exercise = new Exercise();
        when(exerciseRepository.findByName(exerciseName)).thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.findByName(exerciseName);

        assertNotNull(result);
        assertEquals(exercise, result);
        verify(exerciseRepository, times(1)).findByName(exerciseName);
    }

    @Test
    void findByName_whenExerciseDoesNotExist_thenThrowException() {
        String exerciseName = "UnknownExercise";
        when(exerciseRepository.findByName(exerciseName)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ExerciseDoesntExistException.class, () -> exerciseService.findByName(exerciseName));
        assertEquals(ExceptionMessages.EXERCISE_DOESNT_EXIST, exception.getMessage());

        verify(exerciseRepository, times(1)).findByName(exerciseName);
    }

    @Test
    void findAllApprovedExercisesNames_whenExercisesExist_thenReturnNamesList() {
        List<String> names = List.of("Push-up", "Squat");
        when(exerciseRepository.findAllExercisesNamesByStatus(ExerciseStatus.APPROVED)).thenReturn(names);

        List<String> result = exerciseService.findAllApprovedExercisesNames();

        assertEquals(names, result);
        verify(exerciseRepository, times(1)).findAllExercisesNamesByStatus(ExerciseStatus.APPROVED);
    }

    @Test
    void findAllRejectedExercises_whenCalled_thenReturnList() {
        List<Exercise> exercises = List.of(new Exercise());
        when(exerciseRepository.findByStatus(ExerciseStatus.REJECTED)).thenReturn(exercises);

        List<Exercise> result = exerciseService.findAllRejectedExercises();

        assertEquals(exercises, result);
        verify(exerciseRepository, times(1)).findByStatus(ExerciseStatus.REJECTED);
    }

    @Test
    void findAllApprovedExercises_whenCalled_thenReturnList() {
        List<Exercise> exercises = List.of(new Exercise());
        when(exerciseRepository.findByStatus(ExerciseStatus.APPROVED)).thenReturn(exercises);

        List<Exercise> result = exerciseService.findAllApprovedExercises();

        assertEquals(exercises, result);
        verify(exerciseRepository, times(1)).findByStatus(ExerciseStatus.APPROVED);
    }
}
