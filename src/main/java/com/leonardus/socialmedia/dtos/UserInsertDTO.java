package com.leonardus.socialmedia.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInsertDTO {
    @JsonIgnore
    private Long id;
    @NotBlank(message = "The name field must not be blank")
    @Length(min = 3, message = "Tha name field must have at least {min} characters")
    private String name;
    @Email(message = "Email not allowed")
    @NotBlank(message = "The email field must not be blank")
    private String email;
}
