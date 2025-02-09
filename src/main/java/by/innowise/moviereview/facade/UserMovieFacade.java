package by.innowise.moviereview.facade;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.MovieFilterRequest;
import by.innowise.moviereview.dto.MovieResponse;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMovieFacade {
    private final MovieService movieService;
    private final GenreService genreService;
    private final RecommendationService recommendationService;

    public MovieResponse getResponse(Long userId, MovieFilterRequest filterRequest) {
        List<MovieDto> moviesPage = movieService.filterMoviesWithPagination(filterRequest);

        List<EntityDto> genres = genreService.findAll();
        List<MovieDto> recommendations = recommendationService.getRecommendationsForUser(userId);

        return MovieResponse.builder()
                .userId(userId)
                .movies(moviesPage)
                .totalPages(moviesPage.size())
                .genres(genres)
                .recommendations(recommendations)
                .build();
    }
}
