package com.adrianlesniak.redditclone.mapper;

import com.adrianlesniak.redditclone.dto.PostDto;
import com.adrianlesniak.redditclone.dto.PostRequest;
import com.adrianlesniak.redditclone.model.Post;
import com.adrianlesniak.redditclone.model.Subreddit;
import com.adrianlesniak.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "description", source = "postRequest.description")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostDto mapToDto(Post post);
}
