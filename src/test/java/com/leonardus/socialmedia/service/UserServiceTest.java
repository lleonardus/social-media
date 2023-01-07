package com.leonardus.socialmedia.service;

import com.leonardus.socialmedia.dtos.CommentDTO;
import com.leonardus.socialmedia.dtos.PostDTO;
import com.leonardus.socialmedia.dtos.UserDTO;
import com.leonardus.socialmedia.entities.Comment;
import com.leonardus.socialmedia.entities.Post;
import com.leonardus.socialmedia.entities.User;
import com.leonardus.socialmedia.factory.CommentFactory;
import com.leonardus.socialmedia.factory.PostFactory;
import com.leonardus.socialmedia.factory.UserFactory;
import com.leonardus.socialmedia.repositories.CommentRepository;
import com.leonardus.socialmedia.repositories.PostRepository;
import com.leonardus.socialmedia.repositories.UserRepository;
import com.leonardus.socialmedia.service.exceptions.DataIntegrityViolationException;
import com.leonardus.socialmedia.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService service;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    ModelMapper mapper;

    User user;
    UserDTO userDTO;
    Post post;
    PostDTO postDTO;
    Comment comment;
    CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        user = UserFactory.createUser();
        userDTO = UserFactory.createUserDTO();
        post = PostFactory.createPost();
        postDTO = PostFactory.createPostDTO();
        comment = CommentFactory.createComment();
        commentDTO = CommentFactory.createCommentDTO();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        when(postRepository.save(post)).thenReturn(post);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        when(commentRepository.save(comment)).thenReturn(comment);

        doNothing().when(userRepository).delete(user);

        when(mapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(mapper.map(userDTO, User.class)).thenReturn(user);
        when(mapper.map(post, PostDTO.class)).thenReturn(postDTO);
        when(mapper.map(postDTO, Post.class)).thenReturn(post);
        when(mapper.map(comment, CommentDTO.class)).thenReturn(commentDTO);
        when(mapper.map(commentDTO, Comment.class)).thenReturn(comment);
    }

    @Test
    @DisplayName("findAll returns a list of UserDTO")
    void findAll_ReturnsAListOfUserDTO() {
        List<UserDTO> response = service.findAll();

        assertNotNull(response);
        assertEquals(List.of(userDTO), response);
    }

    @Test
    @DisplayName("findById, when user is found, returns an UserDTO")
    void findById_WhenSuccessful_ReturnsAnUserDTO() {
        UserDTO response = service.findById(1L);

        assertNotNull(response);
        assertEquals(userDTO, response);
    }

    @Test
    @DisplayName("findById, when user is not found, throws an ObjectNotFoundException")
    void findById_WhenNotSuccessful_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.findById(2L));
    }

    @Test
    @DisplayName("create, when email is unique, returns an UserDTO")
    void create_WhenEmailIsUnique_ReturnsAnUserDTO() {
        when(mapper.map(any(), any())).thenReturn(userDTO);

        UserDTO response = service.create(userDTO);

        assertNotNull(response);
        assertEquals(userDTO, response);
    }

    @Test
    @DisplayName("create, when email is not unique, throws a DataIntegrityViolationException")
    void create_WhenEmailIsNotUnique_ThrowsADataIntegrityViolationException() {
        when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(User.builder().id(2L).build()));

        assertThrows(DataIntegrityViolationException.class, () -> service.create(userDTO));
    }

    @Test
    @DisplayName("update, when user is found, returns an updated UserDTO")
    void update_WhenSuccessful_ReturnsAnUpdatedUserDTO() {
        userDTO.setName("new name");
        userDTO.setEmail("new email");

        UserDTO response = service.update(1L, userDTO);

        assertNotNull(response);
        assertEquals(userDTO, response);
    }

    @Test
    @DisplayName("update, when email is not unique, throws a DataIntegrityViolationException")
    void update_WhenEmailIsNotUnique_ThrowsADataIntegrityViolationException() {
        when(userRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(User.builder().id(2L).build()));

        assertThrows(DataIntegrityViolationException.class, () -> service.update(1L, userDTO));
    }

    @Test
    @DisplayName("update, when user is not found, throws an ObjectNotFoundException")
    void update_WhenUserIsNotFound_ThrowsAnObjectNotFoundException(){
        assertThrows(ObjectNotFoundException.class, () -> service.update(2L, userDTO));
    }

    @Test
    @DisplayName("deleteById, when users is found, does nothing")
    void deleteById_WhenSuccessful_DoesNothing() {
        service.deleteById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("deleteById, when users is not found, throws an ObjectNotFoundException")
    void deleteById_WhenNotSuccessful_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.deleteById(2L));
    }

    @Test
    @DisplayName("getPosts, when user is found, returns a list of PostDTO")
    void getPosts_WhenSuccessful_ReturnsAListOfPostDTO() {
        List<PostDTO> response = service.getPosts(1L);

        assertNotNull(response);
        assertEquals(List.of(postDTO), response);
    }

    @Test
    @DisplayName("getPosts, when users is not found, throws an ObjectNotFoundException")
    void getPosts_WhenNotSuccessful_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.getPosts(2L));
    }

    @Test
    @DisplayName("createPost, when user is found, returns a PostDTO")
    void createPost_WhenSuccessful_ReturnsAPostDTO() {
        PostDTO response = service.createPost(1L, postDTO);

        assertNotNull(response);
        assertEquals(postDTO, response);
    }

    @Test
    @DisplayName("createPost, when users is not found, throws an ObjectNotFoundException")
    void createPost_WhenNotSuccessful_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.createPost(2L, postDTO));
    }

    @Test
    @DisplayName("getComments, when user is found, returns a list of CommentDTO")
    void getComments_WhenSuccessful_ReturnsAListOfCommentDTO() {
        List<CommentDTO> response = service.getComments(1L);


        assertNotNull(response);
        assertEquals(List.of(commentDTO), response);
    }

    @Test
    @DisplayName("getComments, when users is not found, throws an ObjectNotFoundException")
    void getComments_WhenNotSuccessful_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.getComments(2L));
    }

    @Test
    @DisplayName("createComment, when user is found, returns a CommentDTO")
    void createComment_WhenSuccessful_ReturnsACommentDTO() {
        CommentDTO response = service.createComment(1L, 1L, commentDTO);

        assertNotNull(response);
        assertEquals(commentDTO, response);
    }

    @Test
    @DisplayName("createComment, when user is not found, throws an ObjectNotFoundException")
    void createComment_WhenUserIsNotFound_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.createComment(2L, 1L, commentDTO));
    }

    @Test
    @DisplayName("createComment, when post is not found, throws an ObjectNotFoundException")
    void createComment_WhenPostIsNotFound_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.createComment(1L, 2L, commentDTO));
    }
}