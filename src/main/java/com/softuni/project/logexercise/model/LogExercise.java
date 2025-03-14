package com.softuni.project.logexercise.model;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.log.model.Log;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logs_exercises")
public class LogExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer completedReps;

    @Column(nullable = false)
    private Integer completedSets;

    private Double addedWeight;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "log_id", nullable = false)
    private Log log;
}
