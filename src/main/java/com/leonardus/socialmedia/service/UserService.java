package com.leonardus.socialmedia.service;

import com.leonardus.socialmedia.dtos.*;
import com.leonardus.socialmedia.entities.Comment;
import com.leonardus.socialmedia.entities.Post;
import com.leonardus.socialmedia.entities.User;
import com.leonardus.socialmedia.repositories.CommentRepository;
import com.leonardus.socialmedia.repositories.PostRepository;
import com.leonardus.socialmedia.repositories.UserRepository;
import com.leonardus.socialmedia.service.exceptions.DataIntegrityViolationException;
import com.leonardus.socialmedia.service.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    PostRepository postRepository;
    CommentRepository commentRepository;
    ModelMapper mapper;

    public List<UserDTO> findAll(){
        return userRepository.findAll().stream().map(user -> mapper.map(user, UserDTO.class)).toList();
    }

    public UserDTO findById(Long userId){
        User user = this.findByIdOrElseThrowObjectNotFoundException(userId);

        return mapper.map(user, UserDTO.class);
    }

    public UserDTO create(UserInsertDTO userInsertDTO){
        this.isEmailUnique(userInsertDTO);
        User user = User.builder()
                .name(userInsertDTO.getName())
                .email(userInsertDTO.getEmail())
                .build();

        return mapper.map(userRepository.save(user), UserDTO.class);
    }

    public UserDTO update(Long userId, UserInsertDTO userInsertDTO){
        User user = this.findByIdOrElseThrowObjectNotFoundException(userId);

        userInsertDTO.setId(userId);
        this.isEmailUnique(userInsertDTO);
        user.setName(userInsertDTO.getName());
        user.setEmail(userInsertDTO.getEmail());
        userRepository.save(user);

        return mapper.map(user, UserDTO.class);
    }

    public void deleteById(Long userId){
        User user = this.findByIdOrElseThrowObjectNotFoundException(userId);
        userRepository.delete(user);
    }

    public List<PostDTO> getPosts(Long userId){
        return this.findByIdOrElseThrowObjectNotFoundException(userId).getPosts().stream()
                .map(post -> mapper.map(post, PostDTO.class)).toList();
    }

    public PostDTO createPost(Long userId, PostInsertDTO postInsertDTO){
        User user = this.findByIdOrElseThrowObjectNotFoundException(userId);
        Post post = postRepository.save(mapper.map(postInsertDTO, Post.class));

        user.getPosts().add(post);
        userRepository.save(user);

        return mapper.map(post, PostDTO.class);
    }

    public List<CommentDTO> getComments(Long userId){
        return this.findByIdOrElseThrowObjectNotFoundException(userId).getComments().stream()
                .map(comment -> mapper.map(comment, CommentDTO.class)).toList();
    }

    public CommentDTO createComment(Long userId, Long postId, CommentInsertDTO commentInsertDTO){
        User user = findByIdOrElseThrowObjectNotFoundException(userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ObjectNotFoundException("Post not found"));

        Comment comment = commentRepository.save(mapper.map(commentInsertDTO, Comment.class));

        user.getComments().add(comment);
        userRepository.save(user);

        post.getComments().add(comment);
        postRepository.save(post);

        return mapper.map(comment, CommentDTO.class);
    }

    private User findByIdOrElseThrowObjectNotFoundException(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Could not find user"));
    }

    private void isEmailUnique(UserInsertDTO userInsertDTO){
        Optional<User> user = userRepository.findByEmail(userInsertDTO.getEmail());

        if (user.isPresent() && !user.get().getId().equals(userInsertDTO.getId())){
            throw new DataIntegrityViolationException("Email is already registered");
        }
    }
}
