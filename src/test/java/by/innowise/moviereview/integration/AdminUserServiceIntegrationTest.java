package by.innowise.moviereview.integration;

import by.innowise.moviereview.config.TestConfig;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.service.AdminUserService;
import by.innowise.moviereview.util.enums.Role;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
class AdminUserServiceIntegrationTest {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = TestBuilder.createUser("pash", "pasha@gmail.com");
        userRepository.save(testUser);
    }

    @Test
    void shouldGetAllUsers() {
        // given
        //when
        List<UserDto> users = adminUserService.getAllUsers();
        //then
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals("pash", users.get(0).getUsername());
    }

    @Test
    void shouldDeleteUser() {
        // given
        //when
        adminUserService.deleteUser(testUser.getId());
        //then
        Assertions.assertTrue(adminUserService.getAllUsers().isEmpty());
    }

    @Test
    void shouldBlockUser() {
        // given
        //when
        adminUserService.blockUser(testUser.getId());
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        //then
        Assertions.assertTrue(updatedUser.getIsBlocked());
    }

    @Test
    void shouldUnblockUser() {
        // given
        testUser.setIsBlocked(true);
        userRepository.save(testUser);
        //when
        adminUserService.unblockUser(testUser.getId());
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        //then
        Assertions.assertFalse(updatedUser.getIsBlocked());
    }

    @Test
    void shouldPromoteToAdmin() {
        // given
        //when
        adminUserService.promoteToAdmin(testUser.getId());
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        //then
        Assertions.assertEquals(updatedUser.getRole(), Role.ADMIN);
    }
}

