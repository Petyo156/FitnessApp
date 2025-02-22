package com.softuni.project.workout.model;

import com.softuni.project.log.model.Log;
import com.softuni.project.program.model.Status;
import com.softuni.project.workoutschedule.model.WorkoutSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workouts")
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer duration;

    @Column
    private String additionalInfo;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<WorkoutSchedule> workoutSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL)
    private List<Log> logs = new ArrayList<>();

}
