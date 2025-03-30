package com.softuni.project.like.service;

import com.softuni.project.like.client.LikeClient;
import com.softuni.project.like.client.dto.LikeProgramRequest;
import com.softuni.project.program.service.ProgramService;
import com.softuni.project.web.dto.ViewProgramResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LikeService {
    private final LikeClient likeClient;
    private final ProgramService programService;

    @Autowired
    public LikeService(LikeClient likeClient, ProgramService programService) {
        this.likeClient = likeClient;
        this.programService = programService;
    }

    public void likeProgram(UUID programId, UUID likedByUserId, UUID programOwnerId) {
        LikeProgramRequest request = initializeLikeProgramRequest(programId.toString(), likedByUserId.toString(), programOwnerId.toString());

        ResponseEntity<Void> response = likeClient.likeProgram(request);

        if(!response.getStatusCode().is2xxSuccessful()){
            return;
        }
        log.info("[FEIGN CALL SUCCESS] Liking program was successful.");
    }

    public List<ViewProgramResponse> getAllLikedPrograms(UUID id) {
        return programService.getAllLikedPrograms(getAllLikedProgramsIds(id));
    }

    private List<String> getAllLikedProgramsIds(UUID id) {
        ResponseEntity<List<String>> allLikedPrograms = likeClient.getAllLikedPrograms(id.toString());
        log.info("[FEIGN CALL SUCCESS] Retrieved all liked programs.");
        return allLikedPrograms.getBody();
    }

    private LikeProgramRequest initializeLikeProgramRequest(String programId, String likedByUserId, String programOwnerId) {
        return LikeProgramRequest.builder()
                .programId(programId)
                .likedByUserId(likedByUserId)
                .programOwnerId(programOwnerId)
                .build();
    }


}
