package com.leonardus.socialmedia.factory;

import com.leonardus.socialmedia.dtos.UserDTO;
import com.leonardus.socialmedia.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    public static User createUser(){
        return User.builder()
                .id(1L)
                .name("name")
                .email("email@gmail.com")
                .posts(new ArrayList<>(List.of(PostFactory.createPost())))
                .comments(new ArrayList<>(List.of(CommentFactory.createComment())))
                .build();
    }

    public static UserDTO createUserDTO(){
        return UserDTO.builder()
                .id(1L)
                .name("name")
                .email("email@gmail.com")
                .build();
    }
}
