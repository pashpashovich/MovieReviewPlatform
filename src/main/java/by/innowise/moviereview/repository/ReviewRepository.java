package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStatus(ReviewStatus status);

    List<Review> findByMovieIdAndStatus(Long movieId, ReviewStatus status);

    List<Review> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime createdAt);
}
