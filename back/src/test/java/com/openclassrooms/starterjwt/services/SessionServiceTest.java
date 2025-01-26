package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.StackWalker.Option;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private SessionService sessionService;
    
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test   
    public void testCreate() throws Exception {

        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Super session");
        session1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-25"));
        session1.setDescription("Session mortelle");
        session1.setTeacher(new Teacher());
        session1.setUsers(List.of(new User()));
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());
        
        when(sessionRepository.save(session1)).thenReturn(session1);

        Session createdSession = sessionService.create(session1);

        assertEquals(session1, createdSession);
    }

    @Test
    public void testDelete() throws Exception {

        doNothing()
            .when(sessionRepository)
            .deleteById(1L);

        sessionService.delete(1L);   

        verify(sessionRepository).deleteById(1L);
    }

    @Test
    public void testFindAll() throws Exception {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Super session");
        session1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-25"));
        session1.setDescription("Session mortelle");
        session1.setTeacher(new Teacher());
        session1.setUsers(List.of(new User()));
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());

        Session session2 = new Session();
        session1.setId(1L);
        session1.setName("Supra session");
        session1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-09-25"));
        session1.setDescription("Session trop bien");
        session1.setTeacher(new Teacher());
        session1.setUsers(List.of(new User()));
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());

        when(sessionRepository.findAll()).thenReturn(List.of(session1, session2));

        List<Session> sessions = sessionService.findAll();

        assertEquals(2, sessions.size());
        assertEquals(List.of(session1, session2), sessions);
    }

    @Test
    public void testGetById() throws Exception {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Super session");
        session1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-25"));
        session1.setDescription("Session mortelle");
        session1.setTeacher(new Teacher());
        session1.setUsers(List.of(new User()));
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));

        Session session = sessionService.getById(1L);

        assertEquals(session1, session);
    }

    @Test
    public void testUpdate() throws Exception {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Super session");
        session1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-25"));
        session1.setDescription("Session mortelle");
        session1.setTeacher(new Teacher());
        session1.setUsers(List.of(new User()));
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());

        when(sessionRepository.save(session1)).thenReturn(session1);

        Session updatedSession = sessionService.update(1L, session1);

        assertEquals(session1, updatedSession);
    }

    @Test
    public void testParticipate() throws Exception {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Super session");
        session1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-25"));
        session1.setDescription("Session mortelle");
        session1.setTeacher(new Teacher());
        session1.setUsers(List.of());
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setPassword("password");
        user1.setAdmin(false);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        sessionService.participate(1L, 1L);

        Session session1Modified = new Session();
        session1.setId(1L);
        session1.setName("Super session");
        session1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-25"));
        session1.setDescription("Session mortelle");
        session1.setTeacher(new Teacher());
        session1.setUsers(List.of(user1));
        session1.setCreatedAt(LocalDateTime.now());
        session1.setUpdatedAt(LocalDateTime.now());
        when(sessionRepository.save(session1Modified)).thenReturn(session1Modified);

        verify(sessionRepository).save(session1Modified);
    }

}
