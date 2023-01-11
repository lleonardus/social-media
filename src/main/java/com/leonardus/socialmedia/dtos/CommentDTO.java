package com.leonardus.socialmedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
