package service;

import dto.request.BackPasswordRequest;
import dto.request.UpdatePasswordRequest;
import dto.request.UserRequest;
import dto.response.UserResponse;
import entity.User;
import enums.Role;
import exception.AppException;
import exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequest userRequest;

    @BeforeEach
    void setup() {

        user = new User();
        user.setUserId("123");
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("oldPass");
        user.setPhone("123456789");
        user.setAddress("123 street");
        user.setRole(Role.USER);

        userRequest = new UserRequest();
        userRequest.setUsername("john");
        userRequest.setPassword("password123");
        userRequest.setEmail("john@example.com");
        userRequest.setAddress("123 street");
        userRequest.setPhone("123456789");
        userRequest.setRole(Role.USER);
    }

    @Test
    void createUser_success() {
        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(modelMapper.map(userRequest, User.class)).thenReturn(user);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");

        userService.createUser(userRequest);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_usernameExists_shouldThrow() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        AppException thrown = assertThrows(AppException.class, () -> {
            userService.createUser(userRequest);
        });

        assertEquals(ErrorCode.USERNAME_ALREADY_EXISTS, thrown.getErrorCode());
    }

    @Test
    void backPassword_emailNotMatch_shouldThrow() {
        BackPasswordRequest req = new BackPasswordRequest();
        req.setUsername("john");
        req.setEmail("wrong@example.com");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        AppException ex = assertThrows(AppException.class, () -> {
            userService.backPassword(req);
        });

        assertEquals(ErrorCode.EMAIL_NOT_EXISTED, ex.getErrorCode());
    }

    @Test
    void backPassword_success() {
        BackPasswordRequest req = new BackPasswordRequest();
        req.setUsername("john");
        req.setEmail("john@example.com");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedRandomPass");

        String random = userService.backPassword(req);

        assertNotNull(random);
        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_success() {
        UpdatePasswordRequest req = new UpdatePasswordRequest();
        req.setUsername("john");
        req.setNewPassword("newpass");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");

        userService.updatePassword(req);

        verify(userRepository).save(user);
        assertEquals("encodedNewPass", user.getPassword());
    }

    @Test
    void updatePassword_userNotFound_shouldThrow() {
        UpdatePasswordRequest req = new UpdatePasswordRequest();
        req.setUsername("john");
        req.setNewPassword("newpass");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> userService.updatePassword(req));
        assertEquals(ErrorCode.USER_NOT_EXISTED, ex.getErrorCode());
    }

    @Test
    void updateUser_success() {
        when(userRepository.findById("123")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPass");

        // Giả sử modelMapper.map có side effect là update trường trong user
        doAnswer(invocation -> {
            UserRequest req = invocation.getArgument(0);
            User u = invocation.getArgument(1);
            u.setUsername(req.getUsername());
            u.setEmail(req.getEmail());
            return null;
        }).when(modelMapper).map(any(UserRequest.class), any(User.class));

        userService.updateUser("123", userRequest);

        verify(userRepository).save(user);
        assertEquals("encodedPass", user.getPassword());
        assertEquals("john", user.getUsername());
    }


    @Test
    void deleteUser_success() {
        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        userService.deleteUser("123");

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_userNotFound_shouldThrow() {
        when(userRepository.findById("123")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> {
            userService.deleteUser("123");
        });

        assertEquals(ErrorCode.USER_NOT_EXISTED, ex.getErrorCode());
    }

    @Test
    void getUserById_success() {
        when(userRepository.findById("123")).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(new UserResponse());

        UserResponse response = userService.getUserbyId("123");

        assertNotNull(response);
        verify(userRepository).findById("123");
    }

    @Test
    void getUserById_userNotFound_shouldThrow() {
        when(userRepository.findById("123")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> userService.getUserbyId("123"));
        assertEquals(ErrorCode.USER_NOT_EXISTED, ex.getErrorCode());
    }

    @Test
    void getMyInfo_success() {
        // Mock SecurityContextHolder để trả về username "john"
        var authentication = mock(org.springframework.security.core.Authentication.class);
        when(authentication.getName()).thenReturn("john");

        var securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mock static method SecurityContextHolder.getContext()
        try (var mocked = org.mockito.Mockito.mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
            when(modelMapper.map(user, UserResponse.class)).thenReturn(new UserResponse());

            UserResponse response = userService.getMyInfo();

            assertNotNull(response);
            verify(userRepository).findByUsername("john");
        }
    }

    @Test
    void getMyInfo_userNotFound_shouldThrow() {
        var authentication = mock(org.springframework.security.core.Authentication.class);
        when(authentication.getName()).thenReturn("john");

        var securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (var mocked = org.mockito.Mockito.mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

            AppException ex = assertThrows(AppException.class, () -> userService.getMyInfo());
            assertEquals(ErrorCode.USER_NOT_EXISTED, ex.getErrorCode());
        }
    }

    @Test
    void getListUser_success() {
        List<User> userList = List.of(user);
        List<UserResponse> responseList = List.of(new UserResponse());

        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(user, UserResponse.class)).thenReturn(responseList.get(0));

        List<UserResponse> result = userService.getListUser();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

}
