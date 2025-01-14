package by.innowise.moviereview.service;

import by.innowise.moviereview.dao.MovieDao;
import by.innowise.moviereview.dao.RatingDao;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import jakarta.transaction.Transactional;

import java.util.List;

public class RecommendationService {
    private static RecommendationService instance;
    private final RatingDao ratingDao;
    private final MovieDao movieDao;
    private final MovieMapper movieMapper;

    private RecommendationService() {
        this.ratingDao = RatingDao.getInstance();
        this.movieDao = MovieDao.getInstance();
        this.movieMapper = new MovieMapperImpl();
    }

    public static RecommendationService getInstance() {
        if (instance == null)
            instance = new RecommendationService();
        return instance;
    }

    @Transactional
    public List<MovieDto> getRecommendationsForUser(Long userId) {
        List<Movie> topRatedMovies = movieDao.findTopRatedMovies();
        List<MovieDto> topRatedMoviesDto = topRatedMovies.stream()
                .map(movieMapper::toDtoForRecomendations)
                .toList();
        if (userId == null) {
            return topRatedMoviesDto;
        }
        List<Long> likedGenres = ratingDao.findGenresByUserPreferences(userId);
        if (likedGenres.isEmpty()) {
            return topRatedMoviesDto;
        }
        List<Movie> recommendedMovies = movieDao.findByGenres(likedGenres);
        return recommendedMovies.stream()
                .limit(5)
                .map(movieMapper::toDtoForRecomendations)
                .toList();
    }

}
