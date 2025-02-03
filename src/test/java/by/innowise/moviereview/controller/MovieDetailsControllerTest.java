package by.innowise.moviereview.controller;


import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.WatchlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


class MovieDetailsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RatingService ratingService;

    @Mock
    private MovieService movieService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private WatchlistService watchlistService;

    @InjectMocks
    private MovieDetailsController movieDetailsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movieDetailsController).build();
    }

    @Test
    void shouldGetMovieDetailsSuccess() throws Exception {
        // given
        Long movieId = 1L;
        Long userId = 10L;
        MovieDto mockMovie = new MovieDto();
        mockMovie.setId(movieId);
        mockMovie.setTitle("Test Movie");

        when(movieService.getMovieById(movieId)).thenReturn(mockMovie);
        when(ratingService.getAverageRatingForMovie(movieId)).thenReturn(4.5);
        when(reviewService.findApprovedReviewsByMovieId(movieId)).thenReturn(Collections.emptyList());
        when(watchlistService.isMovieInWatchlist(userId, movieId)).thenReturn(true);
        when(ratingService.getRatingByUserAndMovie(userId, movieId)).thenReturn(5);
        //when
        //then
        mockMvc.perform(get("/user/movies/movie/{movieId}", movieId)
                        .param("userId", userId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/movie-details"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userId", "movie", "averageRating", "reviews", "isInList", "rating"));

        verify(movieService, times(1)).getMovieById(movieId);
        verify(ratingService, times(1)).getAverageRatingForMovie(movieId);
        verify(reviewService, times(1)).findApprovedReviewsByMovieId(movieId);
        verify(watchlistService, times(1)).isMovieInWatchlist(userId, movieId);
        verify(ratingService, times(1)).getRatingByUserAndMovie(userId, movieId);
    }

    @Test
    void shouldGetMovieDetailsNotFound() throws Exception {
        // given
        Long movieId = 1L;
        Long userId = 10L;

        when(movieService.getMovieById(movieId)).thenReturn(null);
        //when
        //then
        mockMvc.perform(get("/user/movies/movie/{movieId}", movieId)
                        .param("userId", userId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error/404"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"));

        verify(movieService, times(1)).getMovieById(movieId);
    }
}