package com.softuni.project.program.model;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.user.model.User;
import com.softuni.project.workoutschedule.model.WorkoutSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "programs")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean sharedWithOthers;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<WorkoutSchedule> workoutSchedules;
}
