package by.innowise.moviereview.repository;

import by.innowise.moviereview.config.TestConfig;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.utils.TestBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestBuilder.createUser("testuser", "testuser@example.com");
        userRepository.save(user);
    }

    @Test
    void shouldFindUserByUsername() {
        // given
        //when
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        //then
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void shouldNotFindUserByInvalidUsername() {
        // given
        //when
        Optional<User> foundUser = userRepository.findByUsername("invaliduser");
        //then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        //when
        Optional<User> foundUser = userRepository.findByEmail("testuser@example.com");
        //then
        assertTrue(foundUser.isPresent());
        assertEquals("testuser@example.com", foundUser.get().getEmail());
    }

    @Test
    void shouldNotFindUserByInvalidEmail() {
        // given
        //when
        Optional<User> foundUser = userRepository.findByEmail("wrong@example.com");
        //then
        assertFalse(foundUser.isPresent());
    }

}