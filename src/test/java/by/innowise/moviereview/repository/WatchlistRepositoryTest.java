package by.innowise.moviereview.repository;

import by.innowise.moviereview.config.TestConfig;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.entity.Watchlist;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
@Transactional
class WatchlistRepositoryTest {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    private User user;
    private Movie movie;
    private Watchlist watchlist;

    @BeforeEach
    void setUp() {
        user = TestBuilder.createUser("testuser", "testuser@example.com");
        user = userRepository.save(user);

        Genre genre1 = genreRepository.save(TestBuilder.createGenre("Драмы"));
        Genre genre2 = genreRepository.save(TestBuilder.createGenre("Комедия"));
        movie = TestBuilder.createMovie("Test Movie", Set.of(genre1, genre2));
        movie = movieRepository.save(movie);

        watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setMovie(movie);
        watchlistRepository.save(watchlist);
    }

    @Test
    void shouldFindWatchlistByUserId() {
        // given
        //when
        List<Watchlist> watchlists = watchlistRepository.findByUserId(user.getId());
        //then
        assertFalse(watchlists.isEmpty());
        assertEquals(1, watchlists.size());
        assertEquals(movie.getId(), watchlists.get(0).getMovie().getId());
    }

    @Test
    void shouldNotFindWatchlistForUnknownUser() {
        // given
        //when
        List<Watchlist> watchlists = watchlistRepository.findByUserId(999L);
        //then
        assertTrue(watchlists.isEmpty());
    }

    @Test
    void shouldFindWatchlistByUserIdAndMovieId() {
        // given
        //when
        Optional<Watchlist> foundWatchlist = watchlistRepository.findByUserIdAndMovieId(user.getId(), movie.getId());
        //then
        assertTrue(foundWatchlist.isPresent());
        assertEquals(user.getId(), foundWatchlist.get().getUser().getId());
        assertEquals(movie.getId(), foundWatchlist.get().getMovie().getId());
    }

    @Test
    void shouldNotFindWatchlistByInvalidUserIdAndMovieId() {
        // given
        //when
        Optional<Watchlist> foundWatchlist = watchlistRepository.findByUserIdAndMovieId(999L, 999L);
        //then
        assertFalse(foundWatchlist.isPresent());
    }
}
