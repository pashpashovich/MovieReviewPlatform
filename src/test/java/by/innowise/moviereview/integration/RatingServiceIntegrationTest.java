package by.innowise.moviereview.integration;

import by.innowise.moviereview.dto.RateCreateDto;
import by.innowise.moviereview.dto.RatingDto;
import by.innowise.moviereview.dto.RatingUpdateRequest;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.repository.GenreRepository;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.RatingRepository;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.utils.TestBuilder;
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

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Testcontainers
class RatingServiceIntegrationTest {


    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private GenreRepository genreRepository;


    private User testUser;
    private Movie testMovie;


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
    public void setUp() {
        testUser = TestBuilder.createUser("testuser", "testuser@example.com");
        userRepository.save(testUser);

        Genre genre = TestBuilder.createGenre("Комедия");
        genreRepository.save(genre);

        testMovie = TestBuilder.createMovie("Test Movie", Set.of(genre));

        movieRepository.save(testMovie);
    }

    @Test
    void testAddRating() {
        // given
        //when
        RateCreateDto rateCreateDto = RateCreateDto.builder()
                .userId(testUser.getId())
                .movieId(testMovie.getId())
                .rating(5)
                .build();
        ratingService.saveRating(rateCreateDto);
        Optional<Rating> rating = ratingRepository.findByUserIdAndMovieId(testUser.getId(), testMovie.getId());
        //then
        assertTrue(rating.isPresent());
        assertEquals(5, rating.get().getRating());
    }

    @Test
    void testUpdateRating() throws IllegalArgumentException {
        // given
        //when
        RateCreateDto rateCreateDto1 = RateCreateDto.builder()
                .userId(testUser.getId())
                .movieId(testMovie.getId())
                .rating(5)
                .build();
        RatingUpdateRequest ratingUpdateRequest = RatingUpdateRequest.builder()
                .rating(4)
                .build();
        RatingDto ratingDto = ratingService.saveRating(rateCreateDto1);
        ratingService.updateRating(ratingDto.getId(), ratingUpdateRequest);
        //then
        assertEquals(4, (int) ratingService.getRatingByUserAndMovie(testUser.getId(), testMovie.getId()));
    }
}
