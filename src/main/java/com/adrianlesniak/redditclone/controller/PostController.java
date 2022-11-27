package com.adrianlesniak.redditclone.controller;

import com.adrianlesniak.redditclone.dto.PostDto;
import com.adrianlesniak.redditclone.dto.PostRequest;
import com.adrianlesniak.redditclone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest, Principal principal) {
        postService.save(postRequest, principal);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostDto>> getPostsBySubreddit(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostDto>> getPostsByUsername(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUsername(name));
    }

}
