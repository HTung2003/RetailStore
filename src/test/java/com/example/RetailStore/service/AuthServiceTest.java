package com.example.RetailStore.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.RetailStore.dto.request.IntrospectRequest;
import com.example.RetailStore.dto.request.LoginRequest;
import com.example.RetailStore.dto.request.RefreshRequest;
import com.example.RetailStore.dto.response.LoginResponse;
import com.example.RetailStore.entity.InvalidatedToken;
import com.example.RetailStore.entity.User;
import com.example.RetailStore.enums.Role;
import com.example.RetailStore.exception.AppException;
import com.example.RetailStore.exception.ErrorCode;
import com.example.RetailStore.repository.InvalidatedTokenRepository;
import com.example.RetailStore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @InjectMocks
    private AuthService authService;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);

        authService.SIGNER_KEY = "your-secret-key-your-secret-key";
        authService.VALID_DURATION = 3600;
        authService.REFRESH_DURATION = 36000;
    }

    @Test
    void testAuthenticateSuccess() {
        // Arrange
        String rawPassword = "password123";
        String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10).encode(rawPassword);

        User user = User.builder()
                .username("testuser")
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword(rawPassword);

        // Act
        LoginResponse response = authService.authenticate(request);

        // Assert
        assertTrue(response.isSuccess());
        assertNotNull(response.getToken());
        assertFalse(response.getToken().isEmpty());
    }

    @Test
    void testAuthenticateFailWrongPassword() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .password(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10).encode("correctPassword"))
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongPassword");

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            authService.authenticate(request);
        });
        assertEquals(ErrorCode.INCORRECT_PASSWORD, exception.getErrorCode());
    }

    @Test
    void testAuthenticateFailUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setUsername("notexist");
        request.setPassword("any");

        AppException exception = assertThrows(AppException.class, () -> {
            authService.authenticate(request);
        });
        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void testIntrospectValidToken() throws Exception {
        // Tạo token từ authenticate để có token hợp lệ
        String rawPassword = "password123";
        String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10).encode(rawPassword);

        User user = User.builder()
                .username("testuser")
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword(rawPassword);

        LoginResponse loginResponse = authService.authenticate(loginRequest);
        String token = loginResponse.getToken();

        var introspectResponse = authService.introspect(new IntrospectRequest(token));

        assertTrue(introspectResponse.isSuccess());
    }

    @Test
    void testRefreshToken() throws Exception {
        // Setup user and token like authenticate test
        String rawPassword = "password123";
        String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(10).encode(rawPassword);

        User user = User.builder()
                .username("testuser")
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword(rawPassword);

        LoginResponse loginResponse = authService.authenticate(loginRequest);
        String token = loginResponse.getToken();

        // Giả lập invalidatedTokenRepository không có token cũ
        when(invalidatedTokenRepository.existsById(anyString())).thenReturn(false);
        when(invalidatedTokenRepository.save(any(InvalidatedToken.class))).thenReturn(null);

        RefreshRequest refreshRequest = new RefreshRequest();
        refreshRequest.setToken(token);

        LoginResponse refreshResponse = authService.refreshToken(refreshRequest);

        assertTrue(refreshResponse.isSuccess());
        assertNotNull(refreshResponse.getToken());
        assertNotEquals(token, refreshResponse.getToken()); // Token mới phải khác token cũ
    }
}
