package com.example.RetailStore.controller;

import com.example.RetailStore.dto.request.BackPasswordRequest;
import com.example.RetailStore.dto.request.UpdatePasswordRequest;
import com.example.RetailStore.dto.request.UserRequest;
import com.example.RetailStore.dto.response.ApiResponse;
import com.example.RetailStore.dto.response.UserResponse;
import com.example.RetailStore.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping
    ApiResponse<String> createUser(@RequestBody @Valid UserRequest userRequest) {
        userService.createUser(userRequest);
        return ApiResponse.<String>builder()
                .data("User created successfully")
                .build();
    }

    @GetMapping("/{user_id}")
    ApiResponse<UserResponse> getUser(@PathVariable String user_id) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUserbyId(user_id))
                .build();
    }

    @GetMapping("/get-all-users")
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getListUser())
                .build();
    }

    @PostMapping("/update/{user_id}")
    ApiResponse<String> updateUser(@PathVariable String user_id, @RequestBody @Valid UserRequest userRequest) {
        userService.updateUser(user_id,userRequest);
        return ApiResponse.<String>builder()
                .data("User updated successfully")
                .build();
    }

    @DeleteMapping("/{user_id}")
    ApiResponse<String> deleteUser(@PathVariable String user_id) {
        userService.deleteUser(user_id);
        return ApiResponse.<String>builder()
                .data("User deleted successfully")
                .build();
    }

    @PostMapping("/back-password")
    ApiResponse<String> backPass(@RequestBody BackPasswordRequest request) {
        String pass = userService.backPassword(request);
        return ApiResponse.<String>builder()
                .data("new password :"+pass)
                .build();
    }

    @PostMapping("/update-password")
    ApiResponse<String> updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        userService.updatePassword(request);
        return ApiResponse.<String>builder()
                .data("User has been update password")
                .build();
    }
}
