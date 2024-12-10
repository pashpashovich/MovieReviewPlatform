package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.repository.UserRepositoryImpl;
import by.innowise.moviereview.util.enums.Role;

import java.util.List;

public class AdminUserService {
    private final UserRepositoryImpl userRepository;
    private final UserMapper userMapper;

    public AdminUserService(UserRepositoryImpl userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toListDto(users).stream()
                .filter(userDto -> userDto.getRole().equals(Role.USER))
                .toList();
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId);
        userRepository.delete(user);
    }

    public void blockUser(Long userId) {
        User user = userRepository.findById(userId);
        user.setIsBlocked(true);
        userRepository.update(user);
    }

    public void unblockUser(Long userId) {
        User user = userRepository.findById(userId);
        user.setIsBlocked(false);
        userRepository.update(user);
    }

    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId);
        user.setRole(Role.ADMIN);
        userRepository.update(user);
    }
}

