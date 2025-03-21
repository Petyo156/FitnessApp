package com.softuni.project.mapper;

import com.softuni.project.program.model.Program;
import com.softuni.project.web.dto.ViewProgramResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ProgramToViewProgramResponseMapper implements Mapper<Program, ViewProgramResponse> {
    @Override
    public ViewProgramResponse map(Program program) {
        return ViewProgramResponse.builder()
                .name(program.getName())
                .description(program.getDescription())
                .difficulty(program.getDifficulty())
                .createdOn(program.getCreatedOn())
                .sharedWithOthers(program.getSharedWithOthers())
                .workouts(new ArrayList<>())
                .addedByUsername(program.getUser().getUsername())
                .id(program.getId().toString())
                .addedById(program.getUser().getId().toString())
                .build();
    }
}
