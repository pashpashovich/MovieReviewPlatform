package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.WatchlistDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.entity.Watchlist;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.repository.WatchlistRepository;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WatchlistServiceTest {

    @Mock
    private WatchlistRepository watchlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private WatchlistService watchlistService;

    private User user;
    private Movie movie;
    private Watchlist watchlist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = TestBuilder.createUser("pash", "pash@gmail.com");

        Genre genre = TestBuilder.createGenre("Драма");
        movie = TestBuilder.createMovie("Test Movie", Set.of(genre));

        watchlist = TestBuilder.createWatchList(user, movie);
    }

    @Test
    void shouldAddToWatchlist() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(watchlistRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.empty());
        //when
        watchlistService.addToWatchlist(1L, 1L);
        //then
        verify(watchlistRepository, times(1)).save(any(Watchlist.class));
    }

    @Test
    void shouldNotAddDuplicateToWatchlist() {
        // given
        when(watchlistRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(watchlist));
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                watchlistService.addToWatchlist(1L, 1L));
        //then
        assertEquals("Этот фильм уже есть в списке 'Хочу посмотреть'.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                watchlistService.addToWatchlist(1L, 1L));
        //then
        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMovieNotFound() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                watchlistService.addToWatchlist(1L, 1L));
        //then
        assertEquals("Фильм не найден", exception.getMessage());
    }

    @Test
    void shouldGetWatchlistByUserId() {
        // given
        when(watchlistRepository.findByUserId(1L)).thenReturn(List.of(watchlist));
        //when
        List<WatchlistDto> watchlistDtos = watchlistService.getWatchlistByUserId(1L);
        //then
        assertEquals(1, watchlistDtos.size());
        assertEquals("Test Movie", watchlistDtos.get(0).getMovieTitle());
    }

    @Test
    void shouldRemoveFromWatchlist() {
        // given
        when(watchlistRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(watchlist));
        //when
        watchlistService.removeFromWatchlist(1L, 1L);
        //then
        verify(watchlistRepository, times(1)).delete(watchlist);
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistentMovie() {
        // given
        when(watchlistRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                watchlistService.removeFromWatchlist(1L, 1L));
        //then
        assertEquals("Фильм с таким id 1 не найден.", exception.getMessage());
    }

    @Test
    void shouldReturnTrueIfMovieInWatchlist() {
        // given
        when(watchlistRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(watchlist));
        //when
        //then
        assertTrue(watchlistService.isMovieInWatchlist(1L, 1L));
    }

    @Test
    void shouldReturnFalseIfMovieNotInWatchlist() {
        // given
        when(watchlistRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.empty());
        //when
        //then
        assertFalse(watchlistService.isMovieInWatchlist(1L, 1L));
    }
}
