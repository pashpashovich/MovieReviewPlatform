package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.RatingRepositoryImpl;
import by.innowise.moviereview.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RatingService {
    private final RatingRepositoryImpl ratingRepository;
    private final UserRepositoryImpl userRepository;
    private final MovieRepositoryImpl movieRepository;
    private final MovieMapper movieMapper;

    public RatingService(RatingRepositoryImpl ratingRepository, UserRepositoryImpl userRepository, MovieRepositoryImpl movieRepository, MovieMapper movieMapper) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public void saveOrUpdateRating(Long userId, Long movieId, int ratingValue) {
        Rating existingRating = ratingRepository.findByUserAndMovie(userId, movieId);
        if (existingRating != null) {
            existingRating.setRating(ratingValue);
            existingRating.setUpdatedAt(LocalDateTime.now());
            ratingRepository.update(existingRating);
        } else {
            Rating newRating = new Rating();
            newRating.setUser(userRepository.findById(userId));
            newRating.setMovie(movieRepository.findById(movieId));
            newRating.setRating(ratingValue);
            newRating.setCreatedAt(LocalDateTime.now());
            ratingRepository.save(newRating);
        }
    }

    public Integer getRatingByUserAndMovie(Long userId, Long movieId) {
        Rating rating = ratingRepository.findByUserAndMovie(userId, movieId);
        return rating != null ? rating.getRating() : null;
    }

    public Double getAverageRatingForMovie(Long movieId) {
        return ratingRepository.findAverageRatingByMovieId(movieId);
    }

    public List<MovieDto> findLastRatedMoviesByUser(Long userId) {
        List<Rating> ratings = ratingRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        return ratings.stream()
                .map(Rating::getMovie)
                .map(movieMapper::toDto)
                .toList();
    }
}
