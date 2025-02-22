package com.softuni.project.log.model;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.user.model.User;
import com.softuni.project.workout.model.Workout;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer completedReps;

    private Integer completedSets;

    private Double addedWeight;

    private LocalDateTime completionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;
}
