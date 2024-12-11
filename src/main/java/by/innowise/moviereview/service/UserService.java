package by.innowise.moviereview.service;


import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.exception.BadCredentialsException;
import by.innowise.moviereview.exception.EmailNotAvailableException;
import by.innowise.moviereview.exception.NoAccessException;
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

    public UserDto findById(Long id)
    {
        return userMapper.toDto(userRepository.findById(id));
    }

    public UserDto authenticate(String username, String password) {
        User user = userRepository.findByEmail(username);
        if (user != null && PasswordUtils.verify(password, user.getPassword())) {
            if (Boolean.TRUE.equals(user.getIsBlocked())) throw new NoAccessException("Пользователь заблокирован");
            else return userMapper.toDto(user);
        } else throw new BadCredentialsException("Неверный логин или пароль");
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
        log.info("Новая сущность:" + entityCreate);
        userRepository.save(entityCreate);
    }

}

