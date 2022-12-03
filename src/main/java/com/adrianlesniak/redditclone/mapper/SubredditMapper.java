package com.adrianlesniak.redditclone.mapper;

import com.adrianlesniak.redditclone.dto.SubredditDto;
import com.adrianlesniak.redditclone.model.Post;
import com.adrianlesniak.redditclone.model.Subreddit;
import com.adrianlesniak.redditclone.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    @Mapping(target = "ownerUsername", expression = "java(subreddit.getUser().getUsername())")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto, User user);
}

