package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {
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
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %s не найден", userId)));
        userRepository.delete(user);
    }

    public void blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %s не найден", userId)));
        user.setIsBlocked(true);
        userRepository.save(user);
    }

    public void unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %s не найден", userId)));
        user.setIsBlocked(false);
        userRepository.save(user);
    }

    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %s не найден", userId)));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}

