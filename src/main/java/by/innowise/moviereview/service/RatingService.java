package by.innowise.moviereview.service;

import by.innowise.moviereview.dao.MovieDao;
import by.innowise.moviereview.dao.RatingDao;
import by.innowise.moviereview.dao.UserDao;
import by.innowise.moviereview.entity.Rating;

import java.time.LocalDateTime;

public class RatingService {
    private static RatingService instance;
    private final RatingDao ratingDao;
    private final UserDao userDao;
    private final MovieDao movieDao;

    private RatingService() {
        this.ratingDao = RatingDao.getInstance();
        this.userDao = UserDao.getInstance();
        this.movieDao = MovieDao.getInstance();
    }

    public static RatingService getInstance() {
        if (instance == null)
            instance = new RatingService();
        return instance;
    }

    public void saveOrUpdateRating(Long userId, Long movieId, int ratingValue) {
        Rating existingRating = ratingDao.findByUserAndMovie(userId, movieId);
        if (existingRating != null) {
            existingRating.setRating(ratingValue);
            existingRating.setUpdatedAt(LocalDateTime.now());
            ratingDao.update(existingRating);
        } else {
            Rating newRating = new Rating();
            newRating.setUser(userDao.findById(userId));
            newRating.setMovie(movieDao.findById(movieId));
            newRating.setRating(ratingValue);
            newRating.setCreatedAt(LocalDateTime.now());
            ratingDao.save(newRating);
        }
    }

    public Integer getRatingByUserAndMovie(Long userId, Long movieId) {
        Rating rating = ratingDao.findByUserAndMovie(userId, movieId);
        return rating != null ? rating.getRating() : null;
    }

    public Double getAverageRatingForMovie(Long movieId) {
        return ratingDao.findAverageRatingByMovieId(movieId);
    }
}
