package com.softuni.project.web.controllers;

import com.softuni.project.excersise.service.ExercisesService;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/exercises")
@Slf4j
public class ExercisesController {
    private final ExercisesService exercisesService;

    @Autowired
    public ExercisesController(ExercisesService exercisesService) {
        this.exercisesService = exercisesService;
    }

    @GetMapping("/")
    public ModelAndView exercises() {
        ModelAndView modelAndView = new ModelAndView("user/exercises");//TODO
        modelAndView.addObject("submitExerciseRequest", new SubmitExerciseRequest());

        return modelAndView;
    }

    @GetMapping("/submit")
    public ModelAndView submitExercise() {
        ModelAndView modelAndView = new ModelAndView("user/submit-exercise");
        modelAndView.addObject("submitExerciseRequest", new SubmitExerciseRequest());

        return modelAndView;
    }

    @PostMapping("/submit")
    public String submitExercise(@Valid SubmitExerciseRequest submitExerciseRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/submit-exercise";
        }

        exercisesService.submitExercise(submitExerciseRequest);//TODO
        return "redirect:/exercises";
    }




}
