package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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

    @Captor
    ArgumentCaptor<Session> sessionCaptor;

    private SessionService sessionService;
    private Session session1;
    private Session session2;
    private User user1;
    private User user2;
    private User user3;
    
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        sessionService = new SessionService(sessionRepository, userRepository);

        session1 = new Session()
        .setId(1L)
        .setName("Super session")
        .setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-25"))
        .setDescription("Session mortelle")
        .setTeacher(new Teacher())
        .setUsers(new ArrayList<User>())
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        session2 = new Session()
        .setId(2L)
        .setName("Supra session")
        .setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2025-09-25"))
        .setDescription("Session trop bien")
        .setTeacher(new Teacher())
        .setUsers(new ArrayList<User>())
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        user1 = new User()
        .setId(1L)
        .setEmail("test@example.com")
        .setFirstName("John")
        .setLastName("Doe")
        .setPassword("password")
        .setAdmin(false)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        user2 = new User()
        .setId(2L)
        .setEmail("user2@example.com")
        .setFirstName("Jane")
        .setLastName("Doe")
        .setPassword("password")
        .setAdmin(false)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        user3 = new User()
        .setId(3L)
        .setEmail("user3@example.com")
        .setFirstName("Jim")
        .setLastName("Beam")
        .setPassword("password")
        .setAdmin(false)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());
    }

    @Test   
    public void testCreate() throws Exception {

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

        when(sessionRepository.findAll()).thenReturn(List.of(session1, session2));

        List<Session> sessions = sessionService.findAll();

        assertEquals(2, sessions.size());
        assertEquals(List.of(session1, session2), sessions);
    }

    @Test
    public void testGetById() throws Exception {

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));

        Session session = sessionService.getById(1L);

        assertEquals(session1, session);
    }

    @Test
    public void testUpdate() throws Exception {

        sessionService.update(4L, session1);

        verify(sessionRepository).save(sessionCaptor.capture());
        Session sessionCaptorValue = sessionCaptor.getValue();
        assertEquals(4L, sessionCaptorValue.getId());
    }

    @Test
    public void testParticipate() throws Exception {

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        sessionService.participate(1L, 1L);

        verify(sessionRepository).save(sessionCaptor.capture());
        Session sessionCaptorValue = sessionCaptor.getValue();
        assertEquals(user1, sessionCaptorValue.getUsers().get(0));
    }

    @Test
    public void testNoLongerParticipate() throws Exception {

        Session sessionWithSomeParticipants = session1.setUsers(new ArrayList<>(Arrays.asList(user1, user2, user3)));
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(sessionWithSomeParticipants));

        sessionService.noLongerParticipate(1L, 1L);

        verify(sessionRepository).save(sessionCaptor.capture());
        Session sessionCaptorValue = sessionCaptor.getValue();
        assertEquals(new ArrayList<>(Arrays.asList(user2, user3)), sessionCaptorValue.getUsers());
    }
}
