package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User user1;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userService = new UserService(userRepository);

        user1 = new User()
        .setId(1L)
        .setEmail("john.doe@example.com")
        .setLastName("Doe")
        .setFirstName("John")
        .setPassword("password")
        .setAdmin(false);
    }

    @Test
    public void testDelete() {

        userService.delete(1L);   

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testFindById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User userFount = userService.findById(1L);

        assertEquals(user1, userFount);
    }
}

