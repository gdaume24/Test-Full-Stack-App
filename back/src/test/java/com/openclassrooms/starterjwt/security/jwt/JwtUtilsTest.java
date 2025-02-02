package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret = "testSecret";

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs = 3600000; // 1 hour

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils.jwtSecret = jwtSecret;
        jwtUtils.jwtExpirationMs = jwtExpirationMs;
    }

    @Test
    public void testGenerateJwtToken() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtUtils.generateJwtToken(authentication);

        String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        assertEquals("testUser", username);
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("testUser", username);
    }

    @Test
    public void testValidateJwtToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_SignatureException() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "InvalidTestSecret")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }
}
