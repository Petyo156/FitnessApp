package com.softuni.project.excersise.model;

import com.softuni.project.log.model.Log;
import com.softuni.project.user.model.User;
import com.softuni.project.workout.model.Workout;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @ElementCollection
    @CollectionTable(name = "exercise_muscle_groups", joinColumns = @JoinColumn(name = "exercise_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group")
    private List<MuscleGroup> muscleGroups;

    private String mediaUrl;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ExerciseStatus status = ExerciseStatus.PENDING;


//    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
//    private List<Log> logs = new ArrayList<>();

}
