package com.adrianlesniak.redditclone.service;

import com.adrianlesniak.redditclone.dto.VoteDto;
import com.adrianlesniak.redditclone.exception.PostNotFoundException;
import com.adrianlesniak.redditclone.exception.SpringRedditException;
import com.adrianlesniak.redditclone.model.Post;
import com.adrianlesniak.redditclone.model.Vote;
import com.adrianlesniak.redditclone.model.VoteType;
import com.adrianlesniak.redditclone.repository.PostRepository;
import com.adrianlesniak.redditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;

    private final PostRepository postRepository;

    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {

        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType().toString());
        }

        if (voteDto.getVoteType().equals(VoteType.UPVOTE)) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
