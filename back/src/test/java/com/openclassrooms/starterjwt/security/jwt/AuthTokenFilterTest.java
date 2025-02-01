package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.apache.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    private TestAppender testAppender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();

        // Configure the test appender
        testAppender = new TestAppender();
        Logger logger = (Logger) LoggerFactory.getLogger(AuthTokenFilter.class);
        logger.addAppender(testAppender);
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
    public void testDoFilterInternal_AuthenticationException() throws ServletException, IOException {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalid-jwt-token");

        when(jwtUtils.validateJwtToken("valid-jwt-token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid-jwt-token")).thenReturn("testuser");
        // The userDetailsService.loadUserByUsername method will throw an exception

        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Verify the log message
        List<ILoggingEvent> log = testAppender.getLog();
        assertEquals(1, log.size());
        assertEquals("Cannot set user authentication: {}", log.get(0).getMessage());    }
    // }

    // @Test
    // public void testDoFilterInternal_NoJwt() throws ServletException, IOException {
    //     MockHttpServletRequest request = new MockHttpServletRequest();
    //     MockHttpServletResponse response = new MockHttpServletResponse();

    //     authTokenFilter.doFilterInternal(request, response, filterChain);

    //     verify(jwtUtils, times(0)).validateJwtToken(anyString());
    //     verify(jwtUtils, times(0)).getUserNameFromJwtToken(anyString());
    //     verify(userDetailsService, times(0)).loadUserByUsername(anyString());
    //     verify(filterChain, times(1)).doFilter(request, response);

    //     assertNull(SecurityContextHolder.getContext().getAuthentication());
    // }
}

    // Test appender class to capture log events
    class TestAppender extends AppenderBase<ILoggingEvent> {
        private final List<ILoggingEvent> log = new ArrayList<>();

        @Override
        protected void append(ILoggingEvent eventObject) {
            log.add(eventObject);
        }

        public List<ILoggingEvent> getLog() {
            return new ArrayList<>(log);
        }
    }
