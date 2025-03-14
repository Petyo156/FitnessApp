package com.softuni.project.log.repository;

import com.softuni.project.log.model.Log;
import com.softuni.project.logexercise.model.LogExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {
    List<Log> findByUserIdOrderByCompletionDateDesc(UUID userId);


}
