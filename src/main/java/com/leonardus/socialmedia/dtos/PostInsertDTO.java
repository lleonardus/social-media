package com.leonardus.socialmedia.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostInsertDTO {
    @NotBlank(message = "The title field must not be blank")
    private String title;
    @NotBlank(message = "The content field must not be blank")
    private String content;
}
