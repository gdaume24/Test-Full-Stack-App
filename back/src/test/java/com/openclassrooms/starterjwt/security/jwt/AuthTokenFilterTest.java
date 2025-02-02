package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_ValidJwt() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer valid-jwt-token");

        when(jwtUtils.validateJwtToken("valid-jwt-token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid-jwt-token")).thenReturn("testuser");

        UserDetails userDetails = new UserDetailsImpl(1L, "testuser", "John", "Doe", true, "password");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken("valid-jwt-token");
        verify(jwtUtils, times(1)).getUserNameFromJwtToken("valid-jwt-token");
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(filterChain, times(1)).doFilter(request, response);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_ifJwtNullNoSetAuthentication() throws ServletException, IOException {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            // No headerAuthorization
            when(jwtUtils.validateJwtToken(null)).thenReturn(false);
            authTokenFilter.doFilterInternal(request, response, filterChain);

            verify(jwtUtils, times(0)).getUserNameFromJwtToken(any());
        }

        @Test
        public void testDoFilterInternal_ifJwtInvalidNoSetAuthentication() throws ServletException, IOException {
                MockHttpServletRequest request = new MockHttpServletRequest();
                MockHttpServletResponse response = new MockHttpServletResponse();
                // Invalid headerAuthorization
                request.addHeader("Authorization", "Bearer invalid-jwt-token");
                when(jwtUtils.validateJwtToken("invalid-jwt-token")).thenReturn(false);

                authTokenFilter.doFilterInternal(request, response, filterChain);
    
                verify(jwtUtils, times(0)).getUserNameFromJwtToken(any());
            }





    @Test
    public void testParseJwt_BadAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "tactactac");

        String jwt = authTokenFilter.parseJwt(request);
        assertNull(jwt, "Le token JWT devrait être null car le header ne commence pas par 'Bearer '");
    }

    @Test
    public void testParseJwt_NoAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String jwt = authTokenFilter.parseJwt(request);
        assertNull(jwt, "Le token JWT devrait être null car le header n'est pas fourni");
    }
    
    @Test
    public void testParseJwt_GoodAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-jwt-token");

        String jwt = authTokenFilter.parseJwt(request);
        assertEquals("valid-jwt-token", jwt, "Le token JWT devrait être 'valid-jwt-token'");
    }
}
