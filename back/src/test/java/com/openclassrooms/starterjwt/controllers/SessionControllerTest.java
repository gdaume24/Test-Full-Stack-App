package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

public class SessionControllerTest {

    @Mock
    private SessionService sessionService;
    @Mock
    private SessionMapper sessionMapper;


    private MockMvc mockMvc;
    private SessionController sessionController;
    private Session session1;
    private SessionDto session1Dto;
    private User user1;
    private Teacher teacher1;
    String token;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        sessionController = new SessionController(sessionService, sessionMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();
        ResultActions resultActions = this.mockMvc
            .perform(post("/api/auth/login")
                .with(httpBasic("john", "123456")));

        teacher1 = new Teacher()
        .setId(1L)
        .setLastName("Dupont")
        .setFirstName("Jean-Marc")
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        user1 = new User()
        .setId(1L)
        .setEmail("john.doe@example.com")
        .setLastName("Doe")
        .setFirstName("John")
        .setPassword("password")
        .setAdmin(false);

        session1 = new Session()
        .setId(1L)
        .setName("Session Hardcore")
        .setDate(new GregorianCalendar(2034, Calendar.JULY, 11).getTime())
        .setDescription("This is a test session.")
        .setTeacher(teacher1)
        .setUsers(Arrays.asList(user1))
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        session1Dto = new SessionDto(
            1L,
            "Session Hardcore",
            new GregorianCalendar(2034, Calendar.JULY, 11).getTime(),
            1L,
            "This is a test session.",
            Arrays.asList(1L),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    public void testFindById() throws Exception {
        when(sessionService.getById(Long.valueOf("1"))).thenReturn(session1);
        when(sessionMapper.toDto(session1)).thenReturn(session1Dto);

        String expectedDate = new GregorianCalendar(2034, Calendar.JULY, 11).getTime().toString();

        mockMvc.perform(post("/api/session/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Session Hardcore")))
        .andExpect(jsonPath("$.date", is(expectedDate)))
        .andExpect(jsonPath("$.teacher_id", is(1L)))
        .andExpect(jsonPath("$.description", is("This is a test session.")))
        .andExpect(jsonPath("$.users", is("Arrays.asList(1L)")));
    }
}
