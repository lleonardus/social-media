package com.leonardus.socialmedia.controller;

import com.leonardus.socialmedia.controller.exceptions.StandardError;
import com.leonardus.socialmedia.dtos.*;
import com.leonardus.socialmedia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/social-media/users")
@AllArgsConstructor
@Tag(name = "User", description = "All endpoints related to the user")
public class UserController {
    UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserDTO>> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id",
            parameters = @Parameter(name = "userId", example = "1"),
            responses = {
                @ApiResponse(responseCode = "200", description = "OK",
                        content = @Content(schema = @Schema(implementation = UserDTO.class),
                                mediaType = MediaType.APPLICATION_JSON_VALUE)),
                @ApiResponse(responseCode = "404", description = "Not found",
                        content = @Content(schema = @Schema(implementation = StandardError.class),
                                examples = @ExampleObject(),
                                mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public ResponseEntity<UserDTO> findById(@PathVariable Long userId){
        return ResponseEntity.ok().body(userService.findById(userId));
    }

    @PostMapping
    @Operation(summary = "Create user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = @Content(schema = @Schema(implementation = UserDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserInsertDTO userInsertDTO){
        UserDTO user = userService.create(userInsertDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user",
            parameters = @Parameter(name = "userId", example = "1"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = UserDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public ResponseEntity<UserDTO> update(@PathVariable Long userId, @RequestBody @Valid UserInsertDTO userInsertDTO){
        return ResponseEntity.ok().body(userService.update(userId, userInsertDTO));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user",
            parameters = @Parameter(name = "userId", example = "1"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "No content", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public ResponseEntity<UserDTO> deleteById(@PathVariable Long userId){
        userService.deleteById(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/posts")
    @Operation(summary = "Get all user posts",
            parameters = @Parameter(name = "userId", example = "1"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = PostDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public ResponseEntity<List<PostDTO>> getPosts(@PathVariable Long userId){
        return ResponseEntity.ok().body(userService.getPosts(userId));
    }


    @PostMapping("/{userId}/posts")
    @Operation(summary = "Create post",
            parameters = @Parameter(name = "userId", example = "1"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = PostDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public ResponseEntity<PostDTO> createPost(@PathVariable Long userId, @RequestBody @Valid PostInsertDTO postInsertDTO){
        return ResponseEntity.ok().body(userService.createPost(userId, postInsertDTO));
    }

    @GetMapping("/{userId}/comments")
    @Operation(summary = "Get all user comments",
            parameters = @Parameter(name = "userId", example = "1"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = CommentDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long userId){
        return ResponseEntity.ok().body(userService.getComments(userId));
    }

    @PostMapping("/{userId}/{postId}/comment")
    @Operation(summary = "Create comment",
            parameters = {@Parameter(name = "userId", example = "1"), @Parameter(name = "postId", example = "1")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = CommentDTO.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(implementation = StandardError.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE))
            })
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long userId, @PathVariable Long postId,
                                                    @RequestBody @Valid CommentInsertDTO commentInsertDTO){
        return ResponseEntity.ok().body(userService.createComment(userId, postId, commentInsertDTO));
    }
}