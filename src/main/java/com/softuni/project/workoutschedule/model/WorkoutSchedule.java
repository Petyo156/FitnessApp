package com.softuni.project.workoutschedule.model;

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
@Table(
        name = "workout_schedules",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"program_id", "dayOfWeek"})
        }
)//tuka moje i da luja
public class WorkoutSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false, unique = true)
    private Program program;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false, unique = true)
    private Workout workout;
}
