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
import by.innowise.moviereview.util.PasswordUtils;
import by.innowise.moviereview.util.enums.Role;
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
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    public UserDto authenticate(String username, String password) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (Boolean.TRUE.equals(user.getIsBlocked())) {
            throw new NoAccessException("User is blocked");
        }

        if (PasswordUtils.verify(password, user.getPassword())) {
            return userMapper.toDto(user);
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public void register(UserCreateDto userCreateDto) {
        if (userRepository.findByUsername(userCreateDto.getUsername()).isPresent()) {
            throw new UserNotFoundException("Username already exists");
        }

        if (userRepository.findByEmail(userCreateDto.getEmail()).isPresent()) {
            throw new EmailNotAvailableException("Email already exists");
        }

        userCreateDto.setPassword(PasswordUtils.hash(userCreateDto.getPassword()));

        User userEntity = userMapper.toEntityCreate(userCreateDto);
        userEntity.setRole(Role.USER);

        userRepository.save(userEntity);
        log.info("New user created: " + userEntity);
    }
}
