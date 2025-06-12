package com.example.RetailStore.controller;

import com.example.RetailStore.dto.request.IntrospectRequest;
import com.example.RetailStore.dto.request.LogOutRequest;
import com.example.RetailStore.dto.request.LoginRequest;
import com.example.RetailStore.dto.request.RefreshRequest;
import com.example.RetailStore.dto.response.ApiResponse;
import com.example.RetailStore.dto.response.IntrospectResponse;
import com.example.RetailStore.dto.response.LoginResponse;
import com.example.RetailStore.service.AuthService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticateController {

    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .data(authService.authenticate(request))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return ApiResponse.<LoginResponse>builder()
                .data(authService.refreshToken(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .data(authService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogOutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<String>builder()
                .data("Token has been logged out")
                .build();
    }
}
