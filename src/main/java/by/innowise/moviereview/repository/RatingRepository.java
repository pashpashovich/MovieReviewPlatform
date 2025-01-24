package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByUserIdAndMovieId(Long userId, Long movieId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.movie.id = :movieId")
    Double findAverageRatingByMovieId(Long movieId);

    @Query("SELECT DISTINCT g.id FROM Rating r " +
            "JOIN r.movie m " +
            "JOIN m.genres g " +
            "WHERE r.user.id = :userId AND r.rating >= 4")
    List<Long> findGenresByUserPreferences(Long userId);
}
