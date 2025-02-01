package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

public class TeacherControllerTest {
    
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;

    TeacherController teacherController;

    private MockMvc mockMvc;
    private Teacher teacher1;
    private TeacherDto teacher1Dto;
    SimpleDateFormat dateFormat;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        teacherController = new TeacherController(teacherService, teacherMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

        teacher1 = new Teacher()
        .setId(1L)
        .setLastName("Dupont")
        .setFirstName("Jean-Marc")
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        teacher1Dto = new TeacherDto(
            1L,
            "Dupont",
            "Jean-Marc",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    public void testFindById() throws Exception {
        when(teacherService.findById(1L)).thenReturn(teacher1);
        when(teacherMapper.toDto(teacher1)).thenReturn(teacher1Dto);

        mockMvc.perform(get("/api/teacher/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.lastName", is("Dupont")))
        .andExpect(jsonPath("$.firstName", is("Jean-Marc")));
    }

    @Test
    public void testFindByIdIfTeacherNotFound() throws Exception {

        mockMvc.perform(get("/api/teacher/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdReturnBadRequest() throws Exception {

        mockMvc.perform(get("/api/teacher/llllllllll")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindAll() throws Exception {

        when(teacherService.findAll()).thenReturn(List.of(teacher1));
        when(teacherMapper.toDto(List.of(teacher1))).thenReturn(List.of(teacher1Dto));

        mockMvc.perform(get("/api/teacher")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
    }
}
