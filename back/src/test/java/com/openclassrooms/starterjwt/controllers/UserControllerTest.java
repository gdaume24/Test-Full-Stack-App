package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import lombok.NonNull;

public class UserControllerTest {
    
    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserService userService;

    UserController userController;

    private MockMvc mockMvc;
    private User user1;
    private UserDto user1Dto;
    SimpleDateFormat dateFormat;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userController = new UserController(userService, userMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user1 = new User()
        .setId(1L)
        .setEmail("john.doe@example.com")
        .setLastName("Doe")
        .setFirstName("John")
        .setPassword("password")
        .setAdmin(false);

        user1Dto = new UserDto(
            1L,
            "john.doe@example.com",
            "Doe",
            "John",
            false,
            "123456",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    public void testFindById() throws Exception {
        when(userService.findById(1L)).thenReturn(user1);
        when(userMapper.toDto(user1)).thenReturn(user1Dto);

        mockMvc.perform(get("/api/user/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.email", is("john.doe@example.com")))
        .andExpect(jsonPath("$.lastName", is("Doe")))
        .andExpect(jsonPath("$.firstName", is("John")))
        .andExpect(jsonPath("$.admin", is(false)));
    }

    @Test
    public void testFindByIdIfTeacherNotFound() throws Exception {

        mockMvc.perform(get("/api/user/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdReturnBadRequest() throws Exception {

        mockMvc.perform(get("/api/user/llllllllll")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {

        when(userService.findById(1L)).thenReturn(user1);

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
        .id(user1.getId())
        .username(user1.getEmail())
        .firstName(user1.getFirstName())
        .lastName(user1.getLastName())
        .password(user1.getPassword())
        .admin(user1.isAdmin())
        .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/api/user/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

        verify(userService).delete(idCaptor.capture());

        assertEquals(1L, idCaptor.getValue());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {

        mockMvc.perform(delete("/api/user/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserUnhautorized() throws Exception {

        when(userService.findById(1L)).thenReturn(user1); // email : john.doe@example.com

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
        .id(user1.getId())
        .username("hihihi") // username : hihihi
        .firstName(user1.getFirstName())
        .lastName(user1.getLastName())
        .password(user1.getPassword())
        .admin(user1.isAdmin())
        .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/api/user/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteReturnBadRequest() throws Exception {

        mockMvc.perform(delete("/api/user/llllllllll")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }



         
}
