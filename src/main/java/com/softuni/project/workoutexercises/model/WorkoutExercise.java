package com.softuni.project.workoutexercises.model;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.program.model.Program;
import com.softuni.project.workout.model.Workout;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer reps;

    private Integer sets;

    private Double addedWeight;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise program;
}
