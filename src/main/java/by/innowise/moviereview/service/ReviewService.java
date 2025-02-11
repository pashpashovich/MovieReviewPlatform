package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.dto.ReviewRequest;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.enums.ReviewStatus;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.ReviewMapper;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.ReviewRepository;
import by.innowise.moviereview.repository.UserRepository;
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
    private static final int LATEST_REVIEWS_DAYS = 5;

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewMapper reviewMapper;

    public ReviewDto addReview(ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Movie movie = movieRepository.findById(reviewRequest.getMovieId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        Review review = new Review()
                .setUser(user)
                .setMovie(movie)
                .setContent(reviewRequest.getContent())
                .setRating(reviewRequest.getRating())
                .setStatus(ReviewStatus.PENDING)
                .setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        log.info("User's {} review of movie {} added", review.getUser().getId(), review.getMovie().getId());
        return reviewMapper.toDto(saved);
    }

    public List<ReviewDto> findAllPendingReviews() {
        List<Review> entities = reviewRepository.findByStatus(ReviewStatus.PENDING);
        return reviewMapper.toListDto(entities);
    }

    public ReviewDto updateReviewStatus(Long reviewId, String status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Рецензия не найдена."));
        review.setStatus(ReviewStatus.valueOf(status));
        reviewRepository.save(review);
        log.info("Review with ID {} has been changed", reviewId);
        return reviewMapper.toDto(review);
    }

    public List<ReviewDto> findApprovedReviewsByMovieId(Long movieId) {
        List<Review> entities = reviewRepository.findByMovieIdAndStatus(movieId, ReviewStatus.APPROVED);
        return reviewMapper.toListDto(entities);
    }

    @Transactional
    public List<ReviewDto> findRecentReviewsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(LATEST_REVIEWS_DAYS);
        List<Review> entities = reviewRepository.findByUserIdAndCreatedAtAfter(userId, fiveDaysAgo);
        return reviewMapper.toListDto(entities);
    }
}
