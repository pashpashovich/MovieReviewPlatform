package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public List<MovieDto> getRecommendationsForUser(Long userId) {
        List<Movie> topRatedMovies = movieRepository.findTopRatedMovies();
        List<MovieDto> topRatedMoviesDto = topRatedMovies.stream()
                .map(movieMapper::toDtoForRecomendations)
                .toList();
        if (userId == null) {
            return topRatedMoviesDto;
        }
        List<Long> likedGenres = ratingRepository.findGenresByUserPreferences(userId);
        if (likedGenres.isEmpty()) {
            return topRatedMoviesDto;
        }
        List<Movie> recommendedMovies = movieRepository.findByGenres(likedGenres);
        return recommendedMovies.stream()
                .limit(5)
                .map(movieMapper::toDtoForRecomendations)
                .toList();
    }

}
