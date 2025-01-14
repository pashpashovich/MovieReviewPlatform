package by.innowise.moviereview.service;

import by.innowise.moviereview.dao.UserDao;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.mapper.UserMapper;
import by.innowise.moviereview.mapper.UserMapperImpl;
import by.innowise.moviereview.util.daofactory.DaoFactory;
import by.innowise.moviereview.util.enums.Role;

import java.util.List;

public class AdminUserService {
    private static AdminUserService instance;
    private final UserDao userDao;
    private final UserMapper userMapper;

    public static AdminUserService getInstance() {
        if (instance == null)
            instance = new AdminUserService();
        return instance;
    }

    private AdminUserService() {
        this.userDao = (UserDao) DaoFactory.getDAO(UserDao.class);
        this.userMapper = new UserMapperImpl();
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userDao.findAll();
        return userMapper.toListDto(users).stream()
                .filter(userDto -> userDto.getRole().equals(Role.USER))
                .toList();
    }

    public void deleteUser(Long userId) {
        User user = userDao.findById(userId);
        userDao.delete(user);
    }

    public void blockUser(Long userId) {
        User user = userDao.findById(userId);
        user.setIsBlocked(true);
        userDao.update(user);
    }

    public void unblockUser(Long userId) {
        User user = userDao.findById(userId);
        user.setIsBlocked(false);
        userDao.update(user);
    }

    public void promoteToAdmin(Long userId) {
        User user = userDao.findById(userId);
        user.setRole(Role.ADMIN);
        userDao.update(user);
    }
}

