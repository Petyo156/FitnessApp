package com.softuni.project.web.controllers;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
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
    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping()
    public ModelAndView exercises() {
        ModelAndView modelAndView = new ModelAndView("user/exercises");

        List<Exercise> exercises = exerciseService.findAllApprovedExercises();

        modelAndView.addObject("exercises", exercises);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView exercises(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("user/exercises");

        List<Exercise> exercises = exerciseService.findAllApprovedExercises();
        Exercise selectedExercise = exerciseService.findById(UUID.fromString(id));
        exerciseService.throwIfNotApproved(selectedExercise);

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

        List<Exercise> approvedExercises = exerciseService.findAllApprovedExercisesByUserId(authenticationMetadata.getId());
        List<Exercise> pendingExercises = exerciseService.findAllPendingExercisesByUserId(authenticationMetadata.getId());
        List<Exercise> rejectedExercises = exerciseService.findAllRejectedExercisesByUserId(authenticationMetadata.getId());
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

        exerciseService.submitExercise(submitExerciseRequest, authenticationMetadata);

        return "redirect:/home";
    }

}
