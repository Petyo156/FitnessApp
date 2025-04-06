package com.softuni.project.workout.repository;

import com.softuni.project.user.model.User;
import com.softuni.project.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> findAllByAddedBy(User user);

    List<Workout> getWorkoutByAddedBy_UsernameOrderByDuration(String username);
}
