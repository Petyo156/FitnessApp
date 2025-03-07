package com.softuni.project.workoutschedule.repository;

import com.softuni.project.workoutschedule.model.WorkoutSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkoutScheduleRepository extends JpaRepository<WorkoutSchedule, UUID> {
}
