package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

import lombok.With;

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
    private Date date1;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        sessionController = new SessionController(sessionService, sessionMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();

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

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = simpleDateFormat.parse("2028-09-09");

        session1 = new Session()
        .setId(1L)
        .setName("Session Hardcore")
        .setDate(new Date(2042, 8, 12))
        .setDescription("This is a test session.")
        .setTeacher(teacher1)
        .setUsers(Arrays.asList(user1))
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        session1Dto = new SessionDto(
            1L,
            "Session Hardcore",
            new Date(2042, 8, 12),
            1L,
            "This is a test session.",
            Arrays.asList(1L),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser(username = "Georges", roles = {"USER", "ADMIN"})
    public void testFindById() throws Exception {
        when(sessionService.getById(Long.valueOf("1"))).thenReturn(session1);
        when(sessionMapper.toDto(session1)).thenReturn(session1Dto);
        String ma_date = new Date(2042, 8, 12).toString();
        mockMvc.perform(get("/api/session/1")
        .header("expectedDate", "Georges")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Session Hardcore")))
        .andExpect(jsonPath("$.date", is(ma_date)))
        .andExpect(jsonPath("$.teacher_id", is(1L)))
        .andExpect(jsonPath("$.description", is("This is a test session.")))
        .andExpect(jsonPath("$.users", is("Arrays.asList(1L)")));
    }
}
