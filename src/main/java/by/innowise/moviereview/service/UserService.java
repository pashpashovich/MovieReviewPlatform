package by.innowise.moviereview.service;


import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.exception.EmailNotAvailableException;
import by.innowise.moviereview.exception.UserNotFoundException;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.mapper.UserMapperImpl;
import by.innowise.moviereview.repository.UserRepositoryImpl;
import by.innowise.moviereview.util.PasswordUtils;
import by.innowise.moviereview.util.enums.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {

    private final UserRepositoryImpl userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepositoryImpl userRepository, UserMapperImpl userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto authenticate(String username, String password) {
        User user = userRepository.findByEmail(username);
        if (user != null && PasswordUtils.verify(password, user.getPassword())) {
            return userMapper.toDto(user);
        }
        return null;
    }

    public void register(UserCreateDto user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserNotFoundException("Пользователь с таким именем уже существует");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailNotAvailableException("Пользователь с таким email уже существует");
        }

        user.setPassword(PasswordUtils.hash(user.getPassword()));
        User entityCreate = userMapper.toEntityCreate(user);
        entityCreate.setRole(Role.USER);
        log.info("Новая сущность:"+entityCreate);
        userRepository.save(entityCreate);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void updateUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        userRepository.update(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}

