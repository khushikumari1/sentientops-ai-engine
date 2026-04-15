package com.sentientops;

import com.sentientops.model.dto.*;
import com.sentientops.model.entity.Incident;
import com.sentientops.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SentientOpsApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Verifies the application context starts successfully
    }

    @Test
    void signupAndLogin() {
        String baseUrl = "http://localhost:" + port + "/api/auth";

        // Signup
        SignupRequest signup = new SignupRequest("testuser", "[email]", "password123");
        ResponseEntity<AuthResponse> signupResp = restTemplate.postForEntity(
                baseUrl + "/signup", signup, AuthResponse.class);
        assertEquals(HttpStatus.CREATED, signupResp.getStatusCode());
        assertNotNull(signupResp.getBody().getToken());

        // Login
        AuthRequest login = new AuthRequest("testuser", "password123");
        ResponseEntity<AuthResponse> loginResp = restTemplate.postForEntity(
                baseUrl + "/login", login, AuthResponse.class);
        assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        assertNotNull(loginResp.getBody().getToken());
    }

    @Test
    void ingestIncidentRequiresAuth() {
        String url = "http://localhost:" + port + "/api/incidents";
        IncidentRequest request = new IncidentRequest("test-svc", "error log", 80.0, 70.0, "HIGH", "test");
        ResponseEntity<String> resp = restTemplate.postForEntity(url, request, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
    }
}
