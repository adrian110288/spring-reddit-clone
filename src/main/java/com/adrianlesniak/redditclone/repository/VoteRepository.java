package com.adrianlesniak.redditclone.repository;

import com.adrianlesniak.redditclone.model.Post;
import com.adrianlesniak.redditclone.model.User;
import com.adrianlesniak.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
