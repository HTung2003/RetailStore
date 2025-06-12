package com.example.RetailStore.service;


import com.example.RetailStore.dto.request.BackPasswordRequest;
import com.example.RetailStore.dto.request.UpdatePasswordRequest;
import com.example.RetailStore.dto.request.UserRequest;
import com.example.RetailStore.dto.response.UserResponse;
import com.example.RetailStore.entity.User;
import com.example.RetailStore.exception.AppException;
import com.example.RetailStore.exception.ErrorCode;
import com.example.RetailStore.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    ModelMapper modelMapper;

    public void createUser(UserRequest userRequest) {

        if(userRepository.existsByUsername(userRequest.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);
    }

    @PostAuthorize("returnObject.username=authentication.name")
    public void updateUser(String user_id,UserRequest userRequest) {
        User user =userRepository.findById(user_id).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        modelMapper.map(userRequest, user);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);
    }

    @PostAuthorize("hasRole('ADMIN')")
    public UserResponse getUserbyId(String user_id) {

        User user = userRepository.findById(user_id).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        return modelMapper.map(user, UserResponse.class);
    }

    @PostAuthorize("returnObject.username=authentication.name")
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
                ()->new AppException(ErrorCode.USER_NOT_EXISTED));
        return modelMapper.map(user, UserResponse.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getListUser(){
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String user_id) {
        User user = userRepository.findById(user_id).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        userRepository.delete(user);
    }

    public String backPassword(BackPasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
        if(!user.getEmail().equals(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_NOT_EXISTED);
        }
        String randomString = generateRandomString(8);
        user.setPassword(passwordEncoder.encode(randomString));
        userRepository.save(user);
        return randomString;
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    @PreAuthorize("#request.username == authentication.name")
    public void  updatePassword(UpdatePasswordRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                ()->new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}
