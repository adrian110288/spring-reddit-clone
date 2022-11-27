package com.adrianlesniak.redditclone.controller;

import com.adrianlesniak.redditclone.dto.VoteDto;
import com.adrianlesniak.redditclone.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto, Principal principal) {
        voteService.vote(voteDto, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
