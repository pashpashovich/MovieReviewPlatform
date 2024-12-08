package by.innowise.moviereview.service;

import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.ReviewRepositoryImpl;
import by.innowise.moviereview.repository.UserRepositoryImpl;
import by.innowise.moviereview.util.enums.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewService {
    private final ReviewRepositoryImpl reviewRepository;
    private final UserRepositoryImpl userRepository;
    private final MovieRepositoryImpl movieRepository;

    public ReviewService() {
        this.reviewRepository = new ReviewRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
        this.movieRepository = new MovieRepositoryImpl();
    }

    public void addReview(Long userId, Long movieId, String content, int rating) {
        User user = userRepository.findById(userId);
        Movie movie = movieRepository.findById(movieId);
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setContent(content);
        review.setRating(rating);
        review.setStatus(ReviewStatus.PENDING);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public List<Review> findAllPendingReviews() {
        return reviewRepository.findByStatus(ReviewStatus.PENDING);
    }

    public void updateReviewStatus(Long reviewId, String status) {
        Review review = reviewRepository.findById(reviewId);
        if (review != null) {
            review.setStatus(ReviewStatus.valueOf(status));
            reviewRepository.update(review);
        } else {
            throw new IllegalArgumentException("Рецензия не найдена.");
        }
    }

    public List<Review> findApprovedReviewsByMovieId(Long movieId) {
        return reviewRepository.findByMovieIdAndStatus(movieId, ReviewStatus.APPROVED);
    }
}
