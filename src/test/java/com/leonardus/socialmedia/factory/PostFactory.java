package com.leonardus.socialmedia.factory;

import com.leonardus.socialmedia.dtos.PostDTO;
import com.leonardus.socialmedia.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class PostFactory {

    public static Post createPost(){
        return Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .comments(new ArrayList<>(List.of(CommentFactory.createComment())))
                .build();
    }

    public static PostDTO createPostDTO(){
        return PostDTO.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();
    }
}
