package com.openclassrooms.starterjwt.controllers;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.Optional;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

public class AuthControllerTest {
    
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    UserRepository userRepository;
    private MockMvc mockMvc;
    private AuthController authController;
    private User user1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        
        user1 = new User()
        .setId(1L)
        .setEmail("john.doe@example.com")
        .setLastName("Doe")
        .setFirstName("John")
        .setPassword("password")
        .setAdmin(true);
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password");
        String content = objectWriter.writeValueAsString(loginRequest);

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
        .id(user1.getId())
        .username(user1.getEmail())
        .firstName(user1.getFirstName())
        .lastName(user1.getLastName())
        .password(user1.getPassword())
        .admin(user1.isAdmin())
        .build();
            
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                                                        loginRequest.getPassword())))
            .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user1));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", is("jwtToken")))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.firstName", is("John")))
            .andExpect(jsonPath("$.lastName", is("Doe")))
            .andExpect(jsonPath("$.username", is("john.doe@example.com")))
            .andExpect(jsonPath("$.admin", is(true)));

    //             // Simuler la requête HTTP POST
    // mockMvc.perform(post("/api/auth/login")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(objectMapper.writeValueAsString(loginRequest)))
    // .andExpect(status().isOk())
    // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    // .andExpect(content().json(
    //     "{" +
    //         "\"token\":\"mocked-jwt-token\"," +
    //         "\"type\":\"Bearer\"," +
    //         "\"id\":1," +
    //         "\"username\":\"admin@test.com\"," +
    //         "\"firstName\":\"John\"," +
    //         "\"lastName\":\"Doe\"," +
    //         "\"admin\":true" +
    //         "}"));
    }
}
