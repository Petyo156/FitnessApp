package com.softuni.project.web.controllers;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExercisesService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/exercises")
@Slf4j
public class ExerciseController {
    private final ExercisesService exercisesService;

    @Autowired
    public ExerciseController(ExercisesService exercisesService) {
        this.exercisesService = exercisesService;
    }

    @GetMapping()
    public ModelAndView exercises() {
        ModelAndView modelAndView = new ModelAndView("user/exercises");

        List<Exercise> exercises = exercisesService.findAllApprovedExercises();

        modelAndView.addObject("exercises", exercises);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView exercises(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("user/exercises");

        List<Exercise> exercises = exercisesService.findAllApprovedExercises();
        Exercise selectedExercise = exercisesService.findById(UUID.fromString(id));
        exercisesService.throwIfNotApproved(selectedExercise);

        modelAndView.addObject("selectedExercise", selectedExercise);
        modelAndView.addObject("exercises", exercises);

        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView submitExercise() {
        ModelAndView modelAndView = new ModelAndView("user/submit-exercise");
        modelAndView.addObject("submitExerciseRequest", new SubmitExerciseRequest());

        return modelAndView;
    }

    @GetMapping("/your-exercises")
    public ModelAndView yourExercises(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/your-exercises");

        List<Exercise> approvedExercises = exercisesService.findAllApprovedExercisesByUserId(authenticationMetadata.getId());
        List<Exercise> pendingExercises = exercisesService.findAllPendingExercisesByUserId(authenticationMetadata.getId());
        List<Exercise> rejectedExercises = exercisesService.findAllRejectedExercisesByUserId(authenticationMetadata.getId());
        log.info("Retrieved all of user's exercises");

        modelAndView.addObject("approvedExercises", approvedExercises);
        modelAndView.addObject("pendingExercises", pendingExercises);
        modelAndView.addObject("rejectedExercises", rejectedExercises);

        return modelAndView;
    }

    @PostMapping("/submit")
    public String submitExercise(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid SubmitExerciseRequest submitExerciseRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/submit-exercise";
        }

        exercisesService.submitExercise(submitExerciseRequest, authenticationMetadata);

        return "redirect:/home";
    }

}
