package com.adrianlesniak.redditclone.controller;

import com.adrianlesniak.redditclone.dto.CommentDto;
import com.adrianlesniak.redditclone.service.CommentsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {

    private CommentsService commentsService;

    @PostMapping
    public ResponseEntity createComment(@RequestBody CommentDto commentDto, Principal principal) {
        commentsService.save(commentDto, principal);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(commentsService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(commentsService.getAllCommentsForUser(userName));
    }


}
