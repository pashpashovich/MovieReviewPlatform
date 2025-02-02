package by.innowise.moviereview.repository;

import by.innowise.moviereview.config.TestConfig;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Rating;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Movie movie1;
    private Movie movie2;
    private User user;

    @BeforeEach
    void setUp() {
        user = TestBuilder.createUser("testuser", "pasha@gmail.com");
        userRepository.save(user);

        Genre genre1 = genreRepository.save(TestBuilder.createGenre("Драмы"));
        Genre genre2 = genreRepository.save(TestBuilder.createGenre("Комедия"));
        movie1 = TestBuilder.createMovie("Movie 1", Set.of(genre1));
        movie2 = TestBuilder.createMovie("Movie 2", Set.of(genre2));
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        Rating rating1 = TestBuilder.createRating(movie1, 5, user);
        Rating rating2 = TestBuilder.createRating(movie2, 3, user);
        ratingRepository.save(rating1);
        ratingRepository.save(rating2);
    }

    @Test
    void shouldFindByUserIdAndMovieId() {
        // given
        //when
        Optional<Rating> rating = ratingRepository.findByUserIdAndMovieId(user.getId(), movie1.getId());
        //then
        assertNotNull(rating);
        assertEquals(5, rating.get().getRating());
    }

    @Test
    void shouldFindAverageRatingByMovieId() {
        // given
        //when
        Double avgRating = ratingRepository.findAverageRatingByMovieId(movie1.getId());
        //then
        assertNotNull(avgRating);
        assertEquals(5.0, avgRating);
    }

    @Test
    void shouldFindGenresByUserPreferences() {
        // given
        List<Long> genres = ratingRepository.findGenresByUserPreferences(user.getId());
        //when
        //then
        assertNotNull(genres);
        assertEquals(1, genres.size());
    }
}
