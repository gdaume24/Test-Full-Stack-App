package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

import lombok.With;

public class SessionControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

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
    SimpleDateFormat dateFormat;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        sessionController = new SessionController(sessionService, sessionMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date1 = dateFormat.parse("2028-09-09");

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
        .setDate(date1)
        .setDescription("This is a test session.")
        .setTeacher(teacher1)
        .setUsers(Arrays.asList(user1))
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        session1Dto = new SessionDto(
            1L,
            "Session Hardcore",
            date1,
            1L,
            "This is a test session.",
            Arrays.asList(1L),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testFindById() throws Exception {
        when(sessionService.getById(Long.valueOf("1"))).thenReturn(session1);
        when(sessionMapper.toDto(session1)).thenReturn(session1Dto);
        ResultActions result = mockMvc.perform(get("/api/session/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Session Hardcore")))
        .andExpect(jsonPath("$.teacher_id", is(1)))
        .andExpect(jsonPath("$.description", is("This is a test session.")))
        .andExpect(jsonPath("$.users[0]", is(1)));

        // Verify date
        String json = result.andReturn().getResponse().getContentAsString();
        Long dateStringReponse = JsonPath.parse(json).read("$.date", Long.class);
        Date dateReponse = new Date(dateStringReponse);
        String formattedDateReponse = dateFormat.format(dateReponse);
        assertEquals(formattedDateReponse, "2028-09-09");
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testFindByIdIfSessionEqualNull() throws Exception {

        ResultActions result = mockMvc.perform(get("/api/session/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testFindByIdWithInvalidId() throws Exception {

        mockMvc.perform(get("/api/session/zzzzzz")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
        }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testFindAll() throws Exception {
    when(this.sessionService.findAll()).thenReturn(Arrays.asList(session1));
    when(this.sessionMapper.toDto(Arrays.asList(session1))).thenReturn(Arrays.asList(session1Dto));

    mockMvc.perform(get("/api/session")
    .contentType(MediaType.APPLICATION_JSON))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].id", is(1)))
    .andExpect(jsonPath("$[0].name", is("Session Hardcore")))
    .andExpect(jsonPath("$[0].teacher_id", is(1)))
    .andExpect(jsonPath("$[0].description", is("This is a test session.")))
    .andExpect(jsonPath("$[0].users[0]", is(1)));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    public void testCreate() throws Exception {
    when(this.sessionMapper.toEntity(session1Dto)).thenReturn(session1);
    when(this.sessionService.create(session1)).thenReturn(session1);
    when(this.sessionMapper.toDto(session1)).thenReturn(session1Dto);

    mockMvc.perform(post("/api/session")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectWriter.writeValueAsString(session1Dto)))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0].id", is(1)))
    .andExpect(jsonPath("$[0].name", is("Session Hardcore")))
    .andExpect(jsonPath("$[0].teacher_id", is(1)))
    .andExpect(jsonPath("$[0].description", is("This is a test session.")))
    .andExpect(jsonPath("$[0].users[0]", is(1)));
    }
}

