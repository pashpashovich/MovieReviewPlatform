package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.exception.BadCredentialsException;
import by.innowise.moviereview.exception.EmailNotAvailableException;
import by.innowise.moviereview.exception.NoAccessException;
import by.innowise.moviereview.exception.UserNotFoundException;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.util.enums.Role;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;
    private UserCreateDto userCreateDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = TestBuilder.createUser("testuser", "test@example.com");

        testUserDto = TestBuilder.createUserDto(1L, "test@example.com", Role.USER, "testuser");

        userCreateDto = UserCreateDto.builder()
                .username("newuser")
                .email("new@example.com")
                .password("newpassword")
                .build();
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);
        //when
        UserDto result = userService.findById(1L);
        //then
        assertNotNull(result);
        assertEquals(testUserDto.getId(), result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.findById(1L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfUserBlocked() {
        // given
        testUser.setIsBlocked(true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        //when
        Exception exception = assertThrows(NoAccessException.class, () -> userService.authenticate("test@example.com", "password123"));
        //then
        assertEquals("User is blocked", exception.getMessage());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // given
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userMapper.toEntityCreate(any(UserCreateDto.class))).thenReturn(new User());
        //when
        userService.register(userCreateDto);
        //then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionIfUsernameExists() {
        // given
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.of(new User()));
        //when
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.register(userCreateDto));
        //then
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionIfEmailExists() {
        // given
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.of(new User()));
        //when
        Exception exception = assertThrows(EmailNotAvailableException.class, () -> userService.register(userCreateDto));
        //then
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
