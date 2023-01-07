package com.leonardus.socialmedia.factory;

import com.leonardus.socialmedia.dtos.CommentDTO;
import com.leonardus.socialmedia.entities.Comment;

public class CommentFactory {

    public static Comment createComment(){
        return Comment.builder()
                .id(1L)
                .content("content")
                .build();
    }

    public static CommentDTO createCommentDTO(){
        return CommentDTO.builder()
                .id(1L)
                .content("content")
                .build();
    }
}
