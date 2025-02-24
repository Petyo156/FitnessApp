package com.softuni.project.excersise.service;

import com.softuni.project.excersise.repository.ExercisesRepository;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExercisesService {
    private final ExercisesRepository exercisesRepository;

    @Autowired
    public ExercisesService(ExercisesRepository exercisesRepository) {
        this.exercisesRepository = exercisesRepository;
    }

    public void submitExercise(@Valid SubmitExerciseRequest submitExerciseRequest) {

    }
}
