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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    private Movie movie1;
    private Movie movie2;
    private Genre genre1;
    private Genre genre2;

    @BeforeEach
    void setUp() {
        genre1 = genreRepository.save(TestBuilder.createGenre("Комедия"));
        genre2 = genreRepository.save(TestBuilder.createGenre("Драма"));

        movie1 = movieRepository.save(TestBuilder.createMovie("Фильм 1", Set.of((genre1))));
        movie2 = movieRepository.save(TestBuilder.createMovie("Фильм 2", Set.of(genre2)));

        User user1 = TestBuilder.createUser("pasha", "pasha@gmail.com");
        User user2 = TestBuilder.createUser("pasha2", "pasha2@gmail.com");

        userRepository.save(user1);
        userRepository.save(user2);

        Rating rating1 = TestBuilder.createRating(movie1, 5, user1);
        Rating rating2 = TestBuilder.createRating(movie2, 3, user2);
        ratingRepository.save(rating1);
        ratingRepository.save(rating2);

        movie1.setRatings(Set.of(rating1));
        movie2.setRatings(Set.of(rating2));
    }

    @Test
    void shouldFindAllMoviesWithGenresAndPeople() {
        // given
        //when
        List<Movie> movies = movieRepository.findAll();
        //then
        assertNotNull(movies);
        assertEquals(2, movies.size());
        assertNotNull(movies.get(0).getGenres());
        assertNull(movies.get(0).getPeople());
    }

    @Test
    void shouldFindMovieByIdWithAllAttributes() {
        // given
        //when
        Optional<Movie> movie = movieRepository.findById(movie1.getId());
        //then
        assertTrue(movie.isPresent());
        assertNotNull(movie.get().getGenres());
        assertNull(movie.get().getPeople());
    }

    @Test
    void shouldFindMoviesByGenres() {
        // given
        //when
        List<Long> genreIds = Arrays.asList(genre1.getId(), genre2.getId());
        List<Movie> movies = movieRepository.findByGenres(genreIds);
        //then
        assertNotNull(movies);
        assertEquals(2, movies.size());
    }

    @Test
    void shouldFindTopRatedMovies() {
        // given
        //when
        List<Movie> topRatedMovies = movieRepository.findTopRatedMovies();
        //then
        assertNotNull(topRatedMovies);
        assertEquals(2, topRatedMovies.size());
        assertTrue(topRatedMovies.get(0).getRatings().size() == topRatedMovies.get(1).getRatings().size());
    }
}