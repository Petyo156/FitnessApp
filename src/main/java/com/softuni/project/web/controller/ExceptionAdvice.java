package com.softuni.project.web.controller;

import com.softuni.project.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
            ExerciseAlreadyExistsException.class,
            UserAlreadyExistsException.class
    })
    public ModelAndView handleDataAlreadyExistExceptions() {

        return new ModelAndView("exception/data-already-exist");
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({
            InvalidWorkoutLogRequestException.class,
            NoSetWorkoutForProgramException.class
    })
    public ModelAndView handleUserRequestsExceptions() {

        return new ModelAndView("exception/data-already-exist");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class,

            ExerciseDoesntExistException.class,
            LogDoesntExistException.class,
            ProgramDoesntExistException.class,
            UserIdDoesntExistException.class,
            UserUsernameDoesntExistException.class,
            WorkoutDoesntExistException.class,

            ExerciseNotApprovedException.class
    })
    public ModelAndView handleNotFoundExceptions() {

        return new ModelAndView("exception/not-found");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("exception/internal-server-error");
        modelAndView.addObject("errorMessage", exception.getClass().getSimpleName());

        return modelAndView;
    }
}
