package com.leonardus.socialmedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonardus.socialmedia.dtos.CommentDTO;
import com.leonardus.socialmedia.dtos.PostDTO;
import com.leonardus.socialmedia.dtos.UserDTO;
import com.leonardus.socialmedia.factory.CommentFactory;
import com.leonardus.socialmedia.factory.PostFactory;
import com.leonardus.socialmedia.factory.UserFactory;
import com.leonardus.socialmedia.service.UserService;
import com.leonardus.socialmedia.service.exceptions.DataIntegrityViolationException;
import com.leonardus.socialmedia.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    public static final String BASE_URL  = "/social-media/users";

    @MockBean
    UserService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    UserDTO userDTO;
    PostDTO postDTO;
    CommentDTO commentDTO;
    String json;

    @BeforeEach
    void setUp() throws Exception{
        userDTO = UserFactory.createUserDTO();
        postDTO = PostFactory.createPostDTO();
        commentDTO = CommentFactory.createCommentDTO();
        json = objectMapper.writeValueAsString(userDTO);

        when(service.findAll()).thenReturn(List.of(userDTO));
        when(service.findById(1L)).thenReturn(userDTO);
        when(service.findById(2L)).thenThrow(ObjectNotFoundException.class);
        when(service.create(userDTO)).thenReturn(userDTO);
        when(service.update(1L, userDTO)).thenReturn(userDTO);
        doNothing().when(service).deleteById(1L);
        doThrow(ObjectNotFoundException.class).when(service).deleteById(2L);
        when(service.getPosts(1L)).thenReturn(List.of(postDTO));
        when(service.getPosts(2L)).thenThrow(ObjectNotFoundException.class);
        when(service.createPost(1L, postDTO)).thenReturn(postDTO);
        when(service.createPost(2L, postDTO)).thenThrow(ObjectNotFoundException.class);
        when(service.getComments(1L)).thenReturn(List.of(commentDTO));
        when(service.getComments(2L)).thenThrow(ObjectNotFoundException.class);
        when(service.createComment(1L, 1L, commentDTO)).thenReturn(commentDTO);
        when(service.createComment(2L, 1L, commentDTO)).thenThrow(ObjectNotFoundException.class);
        when(service.createComment(1L, 2L, commentDTO)).thenThrow(ObjectNotFoundException.class);
    }

    @Test
    @DisplayName("findAll returns 200")
    void findAll_ReturnsAnUserDTO() throws Exception{
        json = objectMapper.writeValueAsString(List.of(userDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("findById, when user is found, returns 200")
    void findById_WhenSuccessful_ReturnsAnUserDTO() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("findById, when user is not found, returns 404")
    void findById_WhenNotSuccessful_ThrowsAnObjectNotFoundException() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("create, when successful, returns 201")
    void create_WhenSuccessful_ReturnsAnUserDTO() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("create, when name is blank, returns 400")
    void create_WhenNameIsBlank_ThrowsAMethodArgumentNotValidException() throws Exception{
        userDTO = UserDTO.builder().name(null).build();
        json = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("create, when name has invalid size, returns 400")
    void create_WhenNameHasInvalidSize_ThrowsAMethodArgumentNotValidException() throws Exception{
        userDTO = UserDTO.builder().name("12").build();
        json = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("create, when email already exists, returns 400")
    void create_WhenEmailAlreadyExists_ThrowsADataIntegrityViolationException() throws Exception{
        when(service.create(userDTO)).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("update, when successful, returns 200")
    void update_WhenSuccessful_ReturnsAnUpdatedUserDTO() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", 1L)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("update, when name is blank, returns 400")
    void update_WhenNameIsBlank_ThrowsAMethodArgumentNotValidException() throws Exception{
        userDTO = UserDTO.builder().name(null).build();
        json = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", 1L)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("update, when name has invalid size, returns 400")
    void update_WhenNameHasInvalidSize_ThrowsAMethodArgumentNotValidException() throws Exception{
        userDTO = UserDTO.builder().name("12").build();
        json = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", 1L)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("update, when email already exists, returns 400")
    void update_WhenEmailAlreadyExists_ThrowsADataIntegrityViolationException() throws Exception{
        when(service.update(1L, userDTO)).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", 1L)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("update, when user is not found, returns 404")
    void update_WhenUserIsNotFound_ThrowsAnObjectNotFoundException() throws Exception{
        when(service.update(1L, userDTO)).thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", 1L)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("deleteById, when user is found, returns 204")
    void deleteById_WhenUserIsFound_Returns204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("deleteById, when user is not found, returns 404")
    void deleteById_WhenUserIsNotFound_ThrowsAnObjectNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("getPosts, when user is found, returns 200")
    void getPosts_WhenUserIsFound_ReturnsAPostDTO() throws Exception {
        List<PostDTO> postDTOS = List.of(postDTO);
        json = objectMapper.writeValueAsString(postDTOS);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}/posts", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("getPosts, when user is not found, returns 404")
    void getPosts_WhenUserIsNotFound_ThrowsAnObjectNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}/posts", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("createPost, when user is found, returns 200")
    void createPost_WhenUserIsFound_ReturnsAPostDTO() throws Exception{
        json = objectMapper.writeValueAsString(postDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{id}/posts", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("createPost, when user is not found, returns 404")
    void createPost_WhenUserIsNotFound_ThrowsAnObjectNotFoundException() throws Exception{
        json = objectMapper.writeValueAsString(postDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{id}/posts", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("createPost, when title is blank, returns 400")
    void createPost_WhenTitleIsBlank_ThrowsAMethodArgumentNotValidException() throws Exception{
        postDTO = PostDTO.builder().title(null).build();
        json = objectMapper.writeValueAsString(postDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{id}/posts", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("createPost, when content is blank, returns 400")
    void createPost_WhenContentIsBlank_ThrowsAMethodArgumentNotValidException() throws Exception{
        postDTO = PostDTO.builder().content(null).build();
        json = objectMapper.writeValueAsString(postDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{id}/posts", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("getComments, when user is found, returns 200")
    void getComments_WhenUserIsFound_ReturnsACommentDTO() throws Exception {
        List<CommentDTO> commentDTOS = List.of(commentDTO);
        json = objectMapper.writeValueAsString(commentDTOS);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("getComments, when user is not found, returns 404")
    void getComments_WhenUserIsNotFound_ThrowsAnObjectNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}/comments", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("createPost, when user is found, returns 200")
    void createComment_WhenUserIsFound_ReturnsAPostDTO() throws Exception{
        json = objectMapper.writeValueAsString(commentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{userId}/{postId}/comment", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("createPost, when user is not found, returns 404")
    void createComment_WhenUserIsNotFound_ThrowsAnObjectNotFoundException() throws Exception{
        json = objectMapper.writeValueAsString(commentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{userId}/{postId}/comment", 2L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("createPost, when post is not found, returns 404")
    void createComment_WhenPostIsNotFound_ThrowsAnObjectNotFoundException() throws Exception{
        json = objectMapper.writeValueAsString(commentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{userId}/{postId}/comment", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("createPost, when content is blank, returns 400")
    void createComment_WhenContentIsBlank_ThrowsAMethodArgumentNotValidException() throws Exception{
        commentDTO = CommentDTO.builder().content(null).build();
        json = objectMapper.writeValueAsString(commentDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/{userId}/{postId}/comment", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}