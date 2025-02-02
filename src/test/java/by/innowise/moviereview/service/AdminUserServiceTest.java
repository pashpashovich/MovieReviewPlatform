package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.util.enums.Role;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminUserService adminUserService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = TestBuilder.createUser("testuser", "testuser@example.com");
        userDto = TestBuilder.createUserDto(user.getId(), user.getEmail(), user.getRole(), user.getUsername());
    }

    @Test
    void shouldGetAllUsers() {
        // given
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toListDto(List.of(user))).thenReturn(List.of(userDto));
        //when
        List<UserDto> users = adminUserService.getAllUsers();
        //then
        assertEquals(1, users.size());
        assertEquals(userDto.getUsername(), users.get(0).getUsername());
    }

    @Test
    void shouldDeleteUser() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //when
        adminUserService.deleteUser(user.getId());
        //then
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundException.class, () -> adminUserService.deleteUser(999L));
    }

    @Test
    void shouldBlockUser() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //when
        adminUserService.blockUser(user.getId());
        //then
        assertTrue(user.getIsBlocked());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUnblockUser() {
        // given
        user.setIsBlocked(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //when
        adminUserService.unblockUser(user.getId());
        //then
        assertFalse(user.getIsBlocked());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldPromoteUserToAdmin() {
        // given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //when
        adminUserService.promoteToAdmin(user.getId());
        //then
        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenBlockingNonExistentUser() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundException.class, () -> adminUserService.blockUser(999L));
    }
}
