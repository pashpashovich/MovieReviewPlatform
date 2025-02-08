package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.MovieFilterRequest;
import by.innowise.moviereview.dto.MovieResponse;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/movies")
@RequiredArgsConstructor
public class UserMovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<MovieResponse> getMovies(
            @PathVariable Long userId,
            @ModelAttribute MovieFilterRequest filterRequest
    ) {
        List<MovieDto> moviesPage = movieService.filterMoviesWithPagination(filterRequest);

        List<EntityDto> genres = genreService.findAll();
        List<MovieDto> recommendations = recommendationService.getRecommendationsForUser(userId);

        MovieResponse response = MovieResponse.builder()
                .userId(userId)
                .movies(moviesPage)
                .totalPages(moviesPage.size())
                .genres(genres)
                .recommendations(recommendations)
                .build();

        return ResponseEntity.ok(response);
    }
}