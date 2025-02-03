package by.innowise.moviereview.integration;

import by.innowise.moviereview.config.TestConfig;
import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.repository.GenreRepository;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.repository.WatchlistRepository;
import by.innowise.moviereview.service.WatchlistService;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
class WatchlistServiceIntegrationTest {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    private User testUser;
    private Movie testMovie;

    @BeforeAll
    void setup() {
        testUser = TestBuilder.createUser("testUser4", "test4@example.com");
        userRepository.save(testUser);
        Genre genre = TestBuilder.createGenre("Комедия");
        genreRepository.save(genre);

        testMovie = TestBuilder.createMovie("Test Movie", Set.of(genre));
        movieRepository.save(testMovie);
    }

    @Test
    void shouldAddMovieToWatchlist() {
        // given
        watchlistService.addToWatchlist(testUser.getId(), testMovie.getId());
        //when
        List<WatchlistDto> watchlist = watchlistService.getWatchlistByUserId(testUser.getId());
        //then
        assertEquals(1, watchlist.size());
        assertEquals(testMovie.getId(), watchlist.get(0).getMovieId());
    }

    @Test
    void shouldRemoveFromWatchlist() {
        // given
        //when
        watchlistService.addToWatchlist(testUser.getId(), testMovie.getId());
        watchlistService.removeFromWatchlist(testUser.getId(), testMovie.getId());
        //then
        assertTrue(watchlistRepository.findByUserIdAndMovieId(testUser.getId(), testMovie.getId()).isEmpty());
    }

    @Test
    void testIsMovieInWatchlist() {
        // given
        //when
        watchlistService.addToWatchlist(testUser.getId(), testMovie.getId());
        //then
        assertTrue(watchlistService.isMovieInWatchlist(testUser.getId(), testMovie.getId()));
    }

    @Test
    void shouldAddDuplicateMovieToWatchlistThrowsException() {
        // given
        watchlistService.addToWatchlist(testUser.getId(), testMovie.getId());
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> watchlistService.addToWatchlist(testUser.getId(), testMovie.getId()));
    }
}