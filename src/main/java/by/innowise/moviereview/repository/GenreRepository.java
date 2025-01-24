package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("SELECT g FROM Genre g WHERE g.name IN :names")
    Set<Genre> findAllByName(@Param("names") Set<String> names);

    @Query("FROM Genre g " +
            "WHERE (:searchQuery IS NULL OR LOWER(g.name) LIKE CONCAT('%', :searchQuery, '%')) " +
            "ORDER BY CASE WHEN :sortField = 'name' THEN g.name END, g.id")
    Page<Genre> findAllWithFilters(@Param("searchQuery") String searchQuery,
                                   Pageable pageable);
}
