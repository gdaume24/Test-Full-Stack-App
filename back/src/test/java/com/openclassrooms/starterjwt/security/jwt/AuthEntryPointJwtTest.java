package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.parser.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AuthenticationException authException;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCommence() throws IOException, ServletException {
        // Mock the request and exception
        when(request.getServletPath()).thenReturn("/test-path");
        when(authException.getMessage()).thenReturn("Unauthorized");
        
        // Use MockHttpServletResponse to capture the response
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // Call the commence method
        authEntryPointJwt.commence(request, mockResponse, authException);

        // Verify the response status and content type
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, mockResponse.getStatus());
        assertEquals(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mockResponse.getContentType());

        // Verify the response body
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody = mapper.readValue(mockResponse.getContentAsString(), new TypeReference<Map<String, Object>>() {});

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseBody.get("status"));
        assertEquals("Unauthorized", responseBody.get("error"));
        assertEquals("Unauthorized", responseBody.get("message"));
        assertEquals("/test-path", responseBody.get("path"));
    }
}
