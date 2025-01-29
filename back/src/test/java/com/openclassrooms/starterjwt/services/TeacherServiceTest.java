package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;

    private Teacher teacher1;
    private Teacher teacher2;
    private Teacher teacher3;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        teacherService = new TeacherService(teacherRepository);

        teacher1 = new Teacher()
            .setId(1L)
            .setFirstName("John")
            .setLastName("Doe");

        teacher2 = new Teacher()
            .setId(2L)
            .setFirstName("Jane")
            .setLastName("Smith");

        teacher3 = new Teacher()
            .setId(3L)
            .setFirstName("Emily")
            .setLastName("Johnson");
    }

    @Test
    public void testFindAll() {

        when(teacherRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(teacher1, teacher2, teacher3)));

        List<Teacher> teacherList = teacherService.findAll();

        assertEquals(List.of(teacher1, teacher2, teacher3), teacherList);
    }

    @Test
    public void testFindById() {

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));

        Teacher teacherFount = teacherService.findById(1L);

        assertEquals(teacher1, teacherFount);
    }
}
