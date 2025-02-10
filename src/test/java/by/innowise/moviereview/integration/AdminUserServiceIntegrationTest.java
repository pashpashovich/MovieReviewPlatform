package by.innowise.moviereview.integration;

import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.enums.Role;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.service.AdminUserService;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Testcontainers
class AdminUserServiceIntegrationTest {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.3")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
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

