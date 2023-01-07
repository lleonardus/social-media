package com.leonardus.socialmedia.controller;

import com.leonardus.socialmedia.dtos.CommentDTO;
import com.leonardus.socialmedia.dtos.PostDTO;
import com.leonardus.socialmedia.dtos.UserDTO;
import com.leonardus.socialmedia.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/social-media/users")
@AllArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long userId){
        return ResponseEntity.ok().body(userService.findById(userId));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserDTO userDTO){
        UserDTO user = userService.create(userDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> update(@PathVariable Long userId, @RequestBody @Valid UserDTO userDTO){
        return ResponseEntity.ok().body(userService.update(userId, userDTO));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDTO> deleteById(@PathVariable Long userId){
        userService.deleteById(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getPosts(@PathVariable Long userId){
        return ResponseEntity.ok().body(userService.getPosts(userId));
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<PostDTO> createPost(@PathVariable Long userId, @RequestBody @Valid PostDTO postDTO){
        return ResponseEntity.ok().body(userService.createPost(userId, postDTO));
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long userId){
        return ResponseEntity.ok().body(userService.getComments(userId));
    }

    @PostMapping("/{userId}/{postId}/comment")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long userId, @PathVariable Long postId,
                                                    @RequestBody @Valid CommentDTO commentDTO){
        return ResponseEntity.ok().body(userService.createComment(userId, postId, commentDTO));
    }
}