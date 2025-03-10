package com.softuni.project.workoutschedule.repository;

import com.softuni.project.workoutschedule.model.WorkoutSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutScheduleRepository extends JpaRepository<WorkoutSchedule, UUID> {
    List<WorkoutSchedule> findAllByProgram_Id(UUID id);
}
