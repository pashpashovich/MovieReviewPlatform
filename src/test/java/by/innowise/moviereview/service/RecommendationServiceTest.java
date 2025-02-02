package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.RatingRepository;
import by.innowise.moviereview.utils.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecommendationServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private RecommendationService recommendationService;

    private List<Movie> topRatedMovies;
    private List<MovieDto> topRatedMoviesDto;
    private List<Movie> recommendedMovies;
    private List<MovieDto> recommendedMoviesDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        topRatedMovies = LongStream.range(1, 4)
                .mapToObj(id -> {
                    Movie movie = new Movie();
                    movie.setId(id);
                    movie.setTitle("Top Movie " + id);
                    return movie;
                })
                .collect(Collectors.toList());

        topRatedMoviesDto = topRatedMovies.stream()
                .map(movie -> {
                    MovieDto dto = new MovieDto();
                    dto.setId(movie.getId());
                    dto.setTitle(movie.getTitle());
                    return dto;
                })
                .collect(Collectors.toList());

        recommendedMovies = LongStream.range(4, 7)
                .mapToObj(id -> {
                    Movie movie = new Movie();
                    movie.setId(id);
                    movie.setTitle("Recommended Movie " + id);
                    return movie;
                })
                .collect(Collectors.toList());

        recommendedMoviesDto = recommendedMovies.stream()
                .map(movie -> {
                    MovieDto dto = new MovieDto();
                    dto.setId(movie.getId());
                    dto.setTitle(movie.getTitle());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Test
    void shouldReturnTopRatedMoviesIfUserIdIsNull() {
        // given
        when(movieRepository.findTopRatedMovies()).thenReturn(topRatedMovies);
        when(movieMapper.toDtoForRecomendations(any(Movie.class)))
                .thenAnswer(invocation -> {
                    Movie movie = invocation.getArgument(0);
                    return TestBuilder.createMovieDto(movie.getTitle());
                });
        //when
        List<MovieDto> result = recommendationService.getRecommendationsForUser(null);
        //then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Top Movie 1", result.get(0).getTitle());
        verify(movieRepository, times(1)).findTopRatedMovies();
        verify(ratingRepository, never()).findGenresByUserPreferences(anyLong());
    }

    @Test
    void shouldReturnTopRatedMoviesIfUserHasNoGenrePreferences() {
        // given
        Long userId = 1L;

        when(movieRepository.findTopRatedMovies()).thenReturn(topRatedMovies);
        when(ratingRepository.findGenresByUserPreferences(userId)).thenReturn(List.of());

        when(movieMapper.toDtoForRecomendations(any(Movie.class)))
                .thenAnswer(invocation -> {
                    Movie movie = invocation.getArgument(0);
                    return TestBuilder.createMovieDto(movie.getTitle());
                });
        //when
        List<MovieDto> result = recommendationService.getRecommendationsForUser(userId);
        //then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(movieRepository, times(1)).findTopRatedMovies();
        verify(ratingRepository, times(1)).findGenresByUserPreferences(userId);
        verify(movieRepository, never()).findByGenres(anyList());
    }

    @Test
    void shouldReturnMoviesBasedOnUserGenrePreferences() {
        // given
        Long userId = 1L;
        List<Long> likedGenres = List.of(1L, 2L);

        when(movieRepository.findTopRatedMovies()).thenReturn(topRatedMovies);
        when(ratingRepository.findGenresByUserPreferences(userId)).thenReturn(likedGenres);
        when(movieRepository.findByGenres(likedGenres)).thenReturn(recommendedMovies);

        when(movieMapper.toDtoForRecomendations(any(Movie.class)))
                .thenAnswer(invocation -> {
                    Movie movie = invocation.getArgument(0);
                    return TestBuilder.createMovieDto(movie.getTitle());
                });
        //when
        List<MovieDto> result = recommendationService.getRecommendationsForUser(userId);
        //then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Recommended Movie 4", result.get(0).getTitle());
        verify(movieRepository, times(1)).findByGenres(likedGenres);
    }
}

