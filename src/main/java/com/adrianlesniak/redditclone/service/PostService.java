package com.adrianlesniak.redditclone.service;

import com.adrianlesniak.redditclone.dto.PostDto;
import com.adrianlesniak.redditclone.dto.PostRequest;
import com.adrianlesniak.redditclone.exception.PostNotFoundException;
import com.adrianlesniak.redditclone.exception.SubredditNotFoundException;
import com.adrianlesniak.redditclone.exception.UsernameNotFoundException;
import com.adrianlesniak.redditclone.mapper.PostMapper;
import com.adrianlesniak.redditclone.model.Post;
import com.adrianlesniak.redditclone.model.Subreddit;
import com.adrianlesniak.redditclone.model.User;
import com.adrianlesniak.redditclone.repository.PostRepository;
import com.adrianlesniak.redditclone.repository.SubredditRepository;
import com.adrianlesniak.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final SubredditRepository subredditRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final AuthService authService;

    private final PostMapper postMapper;

    public void save(PostRequest postRequest, Principal principal) {

        Subreddit subreddit = subredditRepository
                .findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with name " + postRequest.getSubredditName()));

        User user = authService.getCurrentUser(principal);
        Post post = postMapper.map(postRequest, subreddit, user);

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostDto getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + id));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {

        List<Post> posts = postRepository.findAll();

        return posts
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsBySubreddit(Long id) {

        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SubredditNotFoundException("Subreddit not found with id " + id));

        List<Post> allBySubreddit = postRepository.findAllBySubreddit(subreddit);

        return allBySubreddit
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsByUsername(String name) {

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with username not found " + name));

        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
