package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

public class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Spy
    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    Session session1;
    SessionDto session1Dto;
    SimpleDateFormat dateFormat;
    Date date1;
    Teacher teacher1;
    User user1;

    @BeforeEach
    public void setUp() throws Exception  {
        MockitoAnnotations.openMocks(this);

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
            .setName("Session Xtreme")
            .setDate(date1)
            .setDescription("This is a test session.")
            .setTeacher(teacher1)
            .setUsers(Arrays.asList(user1))
            .setCreatedAt(LocalDateTime.now())
            .setUpdatedAt(LocalDateTime.now());

        session1Dto = new SessionDto(
            1L,
            "Session Xtreme",
            date1,
            1L,
            "This is a test session.",
            Arrays.asList(1L),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    public void testToEntity() {

        when(teacherService.findById(1L)).thenReturn(teacher1);
        when(userService.findById(1L)).thenReturn(user1);

        Session session = sessionMapper.toEntity(session1Dto);

        assertEquals(1L, session.getId());
        assertEquals("Session Xtreme", session.getName());
        assertEquals(date1, session.getDate());
        assertEquals("This is a test session.", session.getDescription());
        assertEquals(teacher1, session.getTeacher());
        assertEquals(Arrays.asList(user1), session.getUsers());
    }

    @Test
    public void testToDto() {

        SessionDto session1Dto = sessionMapper.toDto(session1);

        assertEquals(1L, session1Dto.getId());
        assertEquals("Session Xtreme", session1Dto.getName());
        assertEquals(date1, session1Dto.getDate());
        assertEquals(1L, session1Dto.getTeacher_id());
        assertEquals("This is a test session.", session1Dto.getDescription());
        assertEquals(Arrays.asList(1L), session1Dto.getUsers());
        assertEquals(session1.getCreatedAt(), session1Dto.getCreatedAt());
        assertEquals(session1.getUpdatedAt(), session1Dto.getUpdatedAt());
    }
}
