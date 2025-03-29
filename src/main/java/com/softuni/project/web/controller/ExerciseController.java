package com.softuni.project.web.controller;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.security.AuthenticationMetadata;
import com.softuni.project.user.model.User;
import com.softuni.project.user.service.UserService;
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

@Controller
@RequestMapping("/exercises")
@Slf4j
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final UserService userService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService, UserService userService) {
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    @GetMapping()
    public ModelAndView exercises(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/exercises");

        User user = userService.getById(authenticationMetadata.getId());
        List<Exercise> exercises = exerciseService.findAllApprovedExercises();

        modelAndView.addObject("exercises", exercises);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView exercises(@PathVariable String id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/exercises");

        User user = userService.getById(authenticationMetadata.getId());
        List<Exercise> exercises = exerciseService.findAllApprovedExercises();
        Exercise selectedExercise = exerciseService.getById(id);
        exerciseService.throwIfNotApproved(selectedExercise);

        modelAndView.addObject("selectedExercise", selectedExercise);
        modelAndView.addObject("exercises", exercises);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/personal")
    public ModelAndView getUserExercises(@AuthenticationPrincipal AuthenticationMetadata auth) {
        ModelAndView modelAndView = new ModelAndView("user/your-exercises");

        modelAndView.addObject("approvedExercises", exerciseService.findAllApprovedExercisesByUserId(auth.getId()));
        modelAndView.addObject("pendingExercises", exerciseService.findAllPendingExercisesByUserId(auth.getId()));
        modelAndView.addObject("rejectedExercises", exerciseService.findAllRejectedExercisesByUserId(auth.getId()));
        modelAndView.addObject("user", userService.getById(auth.getId()));

        log.info("Retrieved all user's exercises");
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView showSubmitExerciseForm(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/submit-exercise");

        User user = userService.getById(authenticationMetadata.getId());
        modelAndView.addObject("submitExerciseRequest", new SubmitExerciseRequest());
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PostMapping()
    public ModelAndView submitExercise(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                       @Valid @ModelAttribute SubmitExerciseRequest submitExerciseRequest,
                                       BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("user/submit-exercise");
        modelAndView.addObject("user", userService.getById(authenticationMetadata.getId()));

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("submitExerciseRequest", submitExerciseRequest);
            return modelAndView;
        }

        User user = userService.getById(authenticationMetadata.getId());
        exerciseService.submitExercise(submitExerciseRequest, user);

        return new ModelAndView("redirect:/home");
    }

}
