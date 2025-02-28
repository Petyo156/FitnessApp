package com.softuni.project.excersise.repository;

import com.softuni.project.excersise.model.Exercise;
import com.softuni.project.excersise.model.ExerciseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExercisesRepository extends JpaRepository<Exercise, UUID> {

    Optional<Exercise> findByName(String name);

    List<Exercise> findByStatus(ExerciseStatus exerciseStatus);

    List<Exercise> findByStatusAndCreatedBy_Id(ExerciseStatus exerciseStatus, UUID id);
}
