package com.softuni.project.user.model;

import com.softuni.project.log.model.Log;
import com.softuni.project.program.model.Program;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    private boolean isActive;

    private String firstName;

    private String lastName;

    private String profilePicture;

    private String bio;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Program> programs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Log> logs = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Program activeProgram;
}
