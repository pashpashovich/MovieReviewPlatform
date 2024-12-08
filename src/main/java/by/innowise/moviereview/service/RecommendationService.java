package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.RatingRepositoryImpl;

import java.util.List;

public class RecommendationService {
    private final RatingRepositoryImpl ratingRepository;
    private final MovieRepositoryImpl movieRepository;
    private final MovieMapper movieMapper;

    public RecommendationService(RatingRepositoryImpl ratingRepository, MovieRepositoryImpl movieRepository, MovieMapper movieMapper) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public List<MovieDto> getRecommendationsForUser(Long userId) {
        if (userId == null) {
            List<Movie> topRatedMovies = movieRepository.findTopRatedMovies();
            return topRatedMovies.stream()
                    .map(movieMapper::toDto)
                    .toList();
        }
        List<Long> likedGenres = ratingRepository.findGenresByUserPreferences(userId);
        if (likedGenres.isEmpty()) {
            List<Movie> topRatedMovies = movieRepository.findTopRatedMovies();
            return topRatedMovies.stream()
                    .map(movieMapper::toDto)
                    .toList();
        }

        List<Movie> recommendedMovies = movieRepository.findByGenres(likedGenres);

        return recommendedMovies.stream()
                .limit(5)
                .map(movieMapper::toDtoForRecomendations)
                .toList();
    }
}
