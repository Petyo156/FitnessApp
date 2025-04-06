package com.softuni.project.config;

import com.softuni.project.excersise.model.Difficulty;
import com.softuni.project.excersise.model.MuscleGroup;
import com.softuni.project.excersise.service.ExerciseService;
import com.softuni.project.user.service.AdminService;
import com.softuni.project.user.service.UserService;
import com.softuni.project.web.dto.SubmitExerciseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Order(3)
@Component
public class InitializeExercise implements CommandLineRunner {
    private final ExerciseService exerciseService;
    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public InitializeExercise(ExerciseService exerciseService, AdminService adminService, UserService userService) {
        this.exerciseService = exerciseService;
        this.adminService = adminService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!exerciseService.findAllExercises().isEmpty()) {
            return;
        }

        List<SubmitExerciseRequest> exercises = Arrays.asList(
                createExerciseRequest("Bench Press", "A compound exercise for chest, shoulders, and triceps.",
                        List.of(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                        "https://fitnessprogramer.com/wp-content/uploads/2021/02/Barbell-Bench-Press.gif", Difficulty.MEDIUM),

                createExerciseRequest("Deadlift", "A full-body exercise that primarily targets the back and legs.",
                        List.of(MuscleGroup.BACK, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.LOWER_BACK),
                        "https://www.kettlebellkings.com/cdn/shop/articles/barbell-deadlift-movement_1200x1200_crop_center.gif?v=1692228918", Difficulty.HARD),

                createExerciseRequest("Squat", "A fundamental lower-body exercise.",
                        List.of(MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES, MuscleGroup.LOWER_BACK),
                        "https://media.tenor.com/Pfj8vy41k-0AAAAM/gym.gif", Difficulty.MEDIUM),

                createExerciseRequest("Pull-Up", "An upper-body exercise that targets the back and biceps.",
                        List.of(MuscleGroup.BACK, MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
                        "https://media.tenor.com/bOA5VPeUz5QAAAAM/noequipmentexercisesmen-pullups.gif", Difficulty.HARD),

                createExerciseRequest("Plank", "A core-strengthening exercise.",
                        List.of(MuscleGroup.ABS, MuscleGroup.OBLIQUES, MuscleGroup.LOWER_BACK),
                        "https://cdn.jefit.com/assets/img/exercises/gifs/631.gif", Difficulty.EASY),

                createExerciseRequest("Lunges", "A lower-body movement that enhances balance and strength.",
                        List.of(MuscleGroup.QUADS, MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES),
                        "https://media.tenor.com/fWiC9Ze5eUMAAAAM/lunges-exercise.gif", Difficulty.EASY),

                createExerciseRequest("Dips", "A great triceps and chest workout.",
                        List.of(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
                        "https://i.pinimg.com/originals/e7/45/d6/e745d6fcd41963a8a6d36c4b66c009a9.gif", Difficulty.MEDIUM),

                createExerciseRequest("Bicep Curls", "An isolation exercise for the biceps.",
                        List.of(MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
                        "https://i.pinimg.com/originals/7d/3c/de/7d3cdeed84c1c19ad372d5b25beffd08.gif", Difficulty.EASY),

                createExerciseRequest("Shoulder Press", "An upper-body movement for shoulder strength.",
                        List.of(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
                        "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Shoulder-Press.gif", Difficulty.MEDIUM),

                createExerciseRequest("Burpees", "A full-body workout that also improves cardiovascular endurance.",
                        List.of(MuscleGroup.FULL_BODY, MuscleGroup.CARDIO),
                        "https://media1.giphy.com/media/l4pT6Obikzs5gxWSI/giphy.gif?cid=6c09b952h4phlvex1455sdlq7jnqwvxrgyvzk0vb5fhqnlcf&ep=v1_internal_gif_by_id&rid=giphy.gif&ct=g", Difficulty.HARD)
        );

        for (SubmitExerciseRequest s : exercises) {
            exerciseService.submitExercise(s, adminService.getAdmin());
        }

        //Adding exercise by first user
        SubmitExerciseRequest exerciseRequest = createExerciseRequest("Push-up", "Pushing it up.",
                List.of(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                "https://fitnessprogramer.com/wp-content/uploads/2021/06/Push-Up-Plus.gif", Difficulty.EASY);
        exerciseService.submitExercise(exerciseRequest, userService.getFirstBaseUser());
    }

    private SubmitExerciseRequest createExerciseRequest(String name, String description, List<MuscleGroup> muscleGroups, String mediaUrl, Difficulty difficulty) {
        return SubmitExerciseRequest.builder()
                .name(name)
                .description(description)
                .difficulty(difficulty)
                .muscleGroups(muscleGroups)
                .mediaUrl(mediaUrl)
                .build();
    }
}
