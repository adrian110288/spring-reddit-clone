package com.adrianlesniak.redditclone.service;

import com.adrianlesniak.redditclone.dto.CommentDto;
import com.adrianlesniak.redditclone.exception.PostNotFoundException;
import com.adrianlesniak.redditclone.mapper.CommentMapper;
import com.adrianlesniak.redditclone.model.Comment;
import com.adrianlesniak.redditclone.model.Post;
import com.adrianlesniak.redditclone.model.User;
import com.adrianlesniak.redditclone.repository.CommentRepository;
import com.adrianlesniak.redditclone.repository.PostRepository;
import com.adrianlesniak.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class CommentsService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private AuthService authService;

    private CommentMapper commentMapper;

    public void save(CommentDto commentDto, Principal principal) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + commentDto.getPostId()));

        Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser(principal));

        commentRepository.save(comment);
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + postId));

        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
