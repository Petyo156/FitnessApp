package com.softuni.project.like.client;

import com.softuni.project.like.client.dto.LikeProgramRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "likes-svc", url = "http://localhost:9091/api/v1/likes")
public interface LikeClient {

    @PostMapping("/program")
    ResponseEntity<Void> likeProgram(@RequestBody LikeProgramRequest likeProgramRequest);

    @GetMapping("/programs/{userId}")
    ResponseEntity<List<String>> getAllLikedPrograms(@PathVariable("userId") String userId);
}
