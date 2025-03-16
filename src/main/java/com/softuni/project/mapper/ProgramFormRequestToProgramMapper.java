package com.softuni.project.mapper;

import com.softuni.project.program.model.Program;
import com.softuni.project.web.dto.ProgramFormRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProgramFormRequestToProgramMapper implements Mapper<ProgramFormRequest, Program> {
    @Override
    public Program map(ProgramFormRequest programFormRequest) {
        return Program.builder()
                .createdOn(LocalDateTime.now())
                .name(programFormRequest.getName())
                .description(programFormRequest.getDescription())
                .difficulty(programFormRequest.getDifficulty())
                .createdOn(LocalDateTime.now())
                .sharedWithOthers(programFormRequest.getShared())
                .build();
    }
}
