package com.softuni.project.excersise.repository;

import com.softuni.project.excersise.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExercisesRepository extends JpaRepository<Exercise, UUID> {

}
