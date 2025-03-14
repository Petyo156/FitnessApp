package com.softuni.project.scheduler;

import com.softuni.project.excersise.service.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeleteRejectedExercisesScheduler {
    private final ExerciseService exerciseService;

    @Autowired
    public DeleteRejectedExercisesScheduler(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    //After every 15 minutes delete all rejected exercises
    @Scheduled(fixedRate = 900000)//15min
    public void deleteRejectedExercises() {
        exerciseService.deleteAllRejectedExercises();

        log.info("All rejected exercises have been deleted");
    }
}
