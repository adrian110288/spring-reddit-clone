package com.adrianlesniak.redditclone.service;

import com.adrianlesniak.redditclone.dto.SubredditDto;
import com.adrianlesniak.redditclone.exception.SpringRedditException;
import com.adrianlesniak.redditclone.mapper.SubredditMapper;
import com.adrianlesniak.redditclone.model.Subreddit;
import com.adrianlesniak.redditclone.model.User;
import com.adrianlesniak.redditclone.repository.SubredditRepository;
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
public class SubredditService {

    private AuthService authService;

    private final SubredditRepository subredditRepository;

    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto, Principal principal) {

        User user = authService.getCurrentUser(principal);

        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto, user);
        Subreddit savedSubreddit = subredditRepository.save(subreddit);
        subredditDto.setId(savedSubreddit.getId());
        subredditDto.setOwnerUsername(user.getUsername());

        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with id " + id));

        return subredditMapper.mapSubredditToDto(subreddit);
    }

}
