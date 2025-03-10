package com.softuni.project.program.repository;

import com.softuni.project.program.model.Program;
import com.softuni.project.web.dto.ViewProgramResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgramRepository extends JpaRepository<Program, UUID> {

    List<Program> findAllByUser_Id(UUID userId);

    @Query("SELECT p FROM Program p WHERE p.sharedWithOthers = true AND p.user.id <> :id AND p.user.isActive = true ORDER BY p.createdOn DESC")
    List<Program> findSharedProgramsExcludingUser(UUID id);

    @Query("SELECT p FROM Program p WHERE p.sharedWithOthers = true AND p.user.id = :id AND p.user.isActive = true ORDER BY p.createdOn DESC")
    List<Program> getSharedProgramsByUser(UUID id);
}
