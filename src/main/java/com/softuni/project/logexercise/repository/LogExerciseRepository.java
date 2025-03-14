package com.softuni.project.logexercise.repository;

import com.softuni.project.logexercise.model.LogExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogExerciseRepository extends JpaRepository<LogExercise, UUID> {
    List<LogExercise> findLoggedExercisesByLogId(UUID logId);
}
