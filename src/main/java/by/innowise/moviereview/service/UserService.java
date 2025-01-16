package by.innowise.moviereview.service;


import by.innowise.moviereview.dao.UserDao;
import by.innowise.moviereview.dto.UserCreateDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.exception.BadCredentialsException;
import by.innowise.moviereview.exception.EmailNotAvailableException;
import by.innowise.moviereview.exception.NoAccessException;
import by.innowise.moviereview.exception.UserNotFoundException;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.mapper.UserMapperImpl;
import by.innowise.moviereview.util.PasswordUtils;
import by.innowise.moviereview.util.enums.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {
    private static UserService instance;
    private final UserDao userDao;
    private final UserMapper userMapper;

    public UserService() {
        this.userDao = UserDao.getInstance();
        this.userMapper = new UserMapperImpl();
    }

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService();
        return instance;
    }

    public UserDto findById(Long id) {
        return userMapper.toDto(userDao.findById(id));
    }

    public UserDto authenticate(String username, String password) {
        User user = userDao.findByEmail(username);
        if (user != null && PasswordUtils.verify(password, user.getPassword())) {
            if (Boolean.TRUE.equals(user.getIsBlocked())) throw new NoAccessException("Пользователь заблокирован");
            else return userMapper.toDto(user);
        } else throw new BadCredentialsException("Неверный логин или пароль");
    }

    public void register(UserCreateDto user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            throw new UserNotFoundException("Пользователь с таким именем уже существует");
        }

        if (userDao.findByEmail(user.getEmail()) != null) {
            throw new EmailNotAvailableException("Пользователь с таким email уже существует");
        }

        user.setPassword(PasswordUtils.hash(user.getPassword()));
        User entityCreate = userMapper.toEntityCreate(user);
        entityCreate.setRole(Role.USER);
        log.info("Новая сущность:" + entityCreate);
        userDao.save(entityCreate);
    }
}

