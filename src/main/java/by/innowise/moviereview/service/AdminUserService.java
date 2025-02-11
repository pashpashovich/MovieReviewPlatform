package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.enums.Role;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {
    private static final String NOT_FOUND_EXCEPTION_MESSAGE = "Пользователь с ID %s не найден";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toListDto(users).stream()
                .filter(userDto -> userDto.getRole().equals(Role.USER))
                .toList();
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, userId)));
        userRepository.delete(user);
        log.info("User with id {} deleted", userId);
    }

    public void blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, userId)));
        user.setIsBlocked(true);
        userRepository.save(user);
        log.info("User with id {} blocked", userId);
    }

    public void unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, userId)));
        user.setIsBlocked(false);
        userRepository.save(user);
        log.info("User with id {} is unblocked", userId);
    }

    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, userId)));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        log.info("User with id {} has been promoted to administrator role", userId);
    }
}

