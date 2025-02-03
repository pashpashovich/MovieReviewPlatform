package by.innowise.moviereview.service;

import by.innowise.moviereview.entity.Rating;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.RatingRepository;
import by.innowise.moviereview.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public void saveOrUpdateRating(Long userId, Long movieId, int ratingValue) {
        Rating existingRating = ratingRepository.findByUserIdAndMovieId(userId, movieId).orElse(null);
        if (existingRating != null) {
            existingRating.setRating(ratingValue);
            existingRating.setUpdatedAt(LocalDateTime.now());
            ratingRepository.save(existingRating);
            log.info(String.format("User %s's rating for movie %s has been changed to %s",userId,movieId,ratingValue));        } else {
            Rating newRating = new Rating();
            newRating.setUser(userRepository.findById(userId).orElseThrow());
            newRating.setMovie(movieRepository.findById(movieId).orElseThrow());
            newRating.setRating(ratingValue);
            newRating.setCreatedAt(LocalDateTime.now());
            ratingRepository.save(newRating);
            log.info(String.format("User %s rating for movie %s added with tag %s",userId,movieId,ratingValue));        }
    }

    @Transactional
    public Integer getRatingByUserAndMovie(Long userId, Long movieId) {
        return ratingRepository.findByUserIdAndMovieId(userId, movieId)
                .map(Rating::getRating)
                .orElse(null);
    }

    public Double getAverageRatingForMovie(Long movieId) {
        return ratingRepository.findAverageRatingByMovieId(movieId);
    }
}
