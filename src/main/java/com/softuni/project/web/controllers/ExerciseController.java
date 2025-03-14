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
        Exercise selectedExercise = exerciseService.getById(UUID.fromString(id));
        exerciseService.throwIfNotApproved(selectedExercise);

        modelAndView.addObject("selectedExercise", selectedExercise);
        modelAndView.addObject("exercises", exercises);

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView showSubmitExerciseForm() {
        ModelAndView modelAndView = new ModelAndView("user/submit-exercise");
        modelAndView.addObject("submitExerciseRequest", new SubmitExerciseRequest());

        return modelAndView;
    }

    @GetMapping("/personal")
    public ModelAndView getUserExercises(@AuthenticationPrincipal AuthenticationMetadata auth) {
        ModelAndView modelAndView = new ModelAndView("user/your-exercises");
        modelAndView.addObject("approvedExercises", exerciseService.findAllApprovedExercisesByUserId(auth.getId()));
        modelAndView.addObject("pendingExercises", exerciseService.findAllPendingExercisesByUserId(auth.getId()));
        modelAndView.addObject("rejectedExercises", exerciseService.findAllRejectedExercisesByUserId(auth.getId()));

        log.info("Retrieved all user's exercises");
        return modelAndView;
    }

    @PostMapping()
    public String submitExercise(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid SubmitExerciseRequest submitExerciseRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/submit-exercise";
        }

        exerciseService.submitExercise(submitExerciseRequest, authenticationMetadata);

        return "redirect:/home";
    }

}
