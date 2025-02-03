package by.innowise.moviereview.integration;

import by.innowise.moviereview.config.TestConfig;
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
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
public class RatingServiceIntegrationTest {


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

    @BeforeEach
    public void setUp() {
        testUser = TestBuilder.createUser("testuser","testuser@example.com");
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
        ratingService.saveOrUpdateRating(testUser.getId(), testMovie.getId(), 5);
        Optional<Rating> rating = ratingRepository.findByUserIdAndMovieId(testUser.getId(), testMovie.getId());
        //then
        assertTrue(rating.isPresent());
        assertEquals(5, rating.get().getRating());
    }

    @Test
    void testAddDuplicateRating() throws IllegalArgumentException {
        // given
        //when
        ratingService.saveOrUpdateRating(testUser.getId(), testMovie.getId(), 5);
        ratingService.saveOrUpdateRating(testUser.getId(), testMovie.getId(), 4);
        //then
        assertEquals(4, (int) ratingService.getRatingByUserAndMovie(testUser.getId(), testMovie.getId()));
    }
}
