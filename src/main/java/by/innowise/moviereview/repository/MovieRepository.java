package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @Override
    @EntityGraph(attributePaths = {"genres", "people"})
    List<Movie> findAll();

    @Override
    @EntityGraph(attributePaths = {"genres", "people", "watchlist", "ratings", "reviews"})
    Optional<Movie> findById(Long id);

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE g.id IN :genreIds")
    List<Movie> findByGenres(@Param("genreIds") List<Long> genreIds);

    @Query(value = "SELECT m FROM Movie m LEFT JOIN m.ratings r GROUP BY m.id ORDER BY COALESCE(AVG(r.rating), 0) DESC")
    List<Movie> findTopRatedMovies();

}

