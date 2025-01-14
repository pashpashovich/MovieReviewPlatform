package by.innowise.moviereview.service;

import by.innowise.moviereview.dao.MovieDao;
import by.innowise.moviereview.dao.ReviewDao;
import by.innowise.moviereview.dao.UserDao;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.entity.User;
import by.innowise.moviereview.util.enums.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewService {
    private static ReviewService instance;
    private final ReviewDao reviewDao;
    private final UserDao userDao;
    private final MovieDao movieDao;

    private ReviewService() {
        this.reviewDao = ReviewDao.getInstance();
        this.userDao = UserDao.getInstance();
        this.movieDao = MovieDao.getInstance();
    }

    public static ReviewService getInstance() {
        if (instance == null)
            instance = new ReviewService();
        return instance;
    }

    public void addReview(Long userId, Long movieId, String content, int rating) {
        User user = userDao.findById(userId);
        Movie movie = movieDao.findById(movieId);
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setContent(content);
        review.setRating(rating);
        review.setStatus(ReviewStatus.PENDING);
        review.setCreatedAt(LocalDateTime.now());
        reviewDao.save(review);
    }

    public List<Review> findAllPendingReviews() {
        return reviewDao.findByStatus(ReviewStatus.PENDING);
    }

    public void updateReviewStatus(Long reviewId, String status) {
        Review review = reviewDao.findById(reviewId);
        if (review != null) {
            review.setStatus(ReviewStatus.valueOf(status));
            reviewDao.update(review);
        } else {
            throw new IllegalArgumentException("Рецензия не найдена.");
        }
    }

    public List<Review> findApprovedReviewsByMovieId(Long movieId) {
        return reviewDao.findByMovieIdAndStatus(movieId, ReviewStatus.APPROVED);
    }

    public List<Review> findRecentReviewsByUserId(Long userId) {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        return reviewDao.findByUserIdAndCreatedAtAfter(userId, fiveDaysAgo);
    }
}
