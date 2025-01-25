package com.openclassrooms.starterjwt.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.repository.SessionRepository;

public class SessionServiceTest {
    
    SessionRepository sessionRepository;

    @BeforeEach
    public void setUp() {
        sessionRepository = mock(SessionRepository.class);
    }

    @Test
    public void testCreate() {
        when(sessionRepository.save(any()).thenReturn())
    }
}
