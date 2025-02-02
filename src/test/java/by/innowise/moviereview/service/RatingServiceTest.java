package by.innowise.moviereview.service;

import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.RatingRepository;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private RatingService ratingService;

    private User user;
    private Movie movie;
    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = TestBuilder.createUser("testUser", "testUser@gmail.com");

        Genre genre = new Genre();
        movie = TestBuilder.createMovie("Test Movie", Set.of(genre));

        rating = TestBuilder.createRating(movie, 4, user);
    }

    @Test
    void shouldSaveNewRating() {
        // given
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        //when
        ratingService.saveOrUpdateRating(1L, 1L, 5);
        //then
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void shouldUpdateExistingRating() {
        // given
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(rating));
        //when
        ratingService.saveOrUpdateRating(1L, 1L, 5);
        //then
        assertEquals(5, rating.getRating());
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    void shouldReturnUserRatingIfExists() {
        // given
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(rating));
        //when
        Integer retrievedRating = ratingService.getRatingByUserAndMovie(1L, 1L);
        //then
        assertNotNull(retrievedRating);
        assertEquals(4, retrievedRating);
    }

    @Test
    void shouldReturnNullIfUserRatingDoesNotExist() {
        // given
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.empty());
        //when
        Integer retrievedRating = ratingService.getRatingByUserAndMovie(1L, 1L);
        //then
        assertNull(retrievedRating);
    }

    @Test
    void shouldReturnAverageRatingForMovie() {
        // given
        when(ratingRepository.findAverageRatingByMovieId(1L)).thenReturn(4.5);
        //when
        Double averageRating = ratingService.getAverageRatingForMovie(1L);
        //then
        assertNotNull(averageRating);
        assertEquals(4.5, averageRating);
    }
}
