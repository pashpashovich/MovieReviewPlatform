package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.enums.Role;
import by.innowise.moviereview.exception.EmailNotAvailableException;
import by.innowise.moviereview.exception.UserNotFoundException;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    // Исключение UserNotFoundException не подходит по логике, советую создать новое
    public UserDto register(UserCreateDto userCreateDto) {
        if (userRepository.findByUsername(userCreateDto.getUsername()).isPresent()) {
            throw new UserNotFoundException("Username already exists");
        }

        if (userRepository.findByEmail(userCreateDto.getEmail()).isPresent()) {
            throw new EmailNotAvailableException("Email already exists");
        }

        User userEntity = userMapper.toEntityCreate(userCreateDto)
                .setRole(Role.USER)
                .setPassword(PasswordUtils.hash(userCreateDto.getPassword()));

        User savedUser = userRepository.save(userEntity);
        log.info("New user registered: " + userEntity.getUsername());
        return userMapper.toDto(savedUser);
    }
}
