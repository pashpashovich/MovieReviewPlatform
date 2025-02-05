package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.ReviewMapper;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.ReviewRepository;
import by.innowise.moviereview.repository.UserRepository;
import by.innowise.moviereview.util.enums.ReviewStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewMapper reviewMapper;


    public ReviewDto addReview(Long userId, Long movieId, String content, int rating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден."));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Фильм не найден."));
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setContent(content);
        review.setRating(rating);
        review.setStatus(ReviewStatus.PENDING);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        log.info("User's {} review of movie {} added", userId, movieId);
        return reviewMapper.toDto(saved);
    }

    @Transactional
    public List<Review> findAllPendingReviews() {
        return reviewRepository.findByStatus(ReviewStatus.PENDING);
    }

    public ReviewDto updateReviewStatus(Long reviewId, String status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Рецензия не найдена."));
        review.setStatus(ReviewStatus.valueOf(status));
        reviewRepository.save(review);
        log.info(String.format("Review with ID %s has been changed", reviewId));
        return reviewMapper.toDto(review);
    }

    @Transactional
    public List<Review> findApprovedReviewsByMovieId(Long movieId) {
        return reviewRepository.findByMovieIdAndStatus(movieId, ReviewStatus.APPROVED);
    }

    @Transactional
    public List<Review> findRecentReviewsByUserId(Long userId) {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        return reviewRepository.findByUserIdAndCreatedAtAfter(userId, fiveDaysAgo);
    }
}
