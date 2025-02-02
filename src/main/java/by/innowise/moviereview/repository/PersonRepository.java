package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.enums.MovieRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByRole(MovieRole role);

    @Query("SELECT p FROM Person p WHERE " +
            "(:searchQuery IS NULL OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) AND " +
            "(:roleFilter IS NULL OR p.role = :roleFilter)")
    Page<Person> findWithFilters(@Param("searchQuery") String searchQuery,
                                 @Param("roleFilter") MovieRole roleFilter,
                                 Pageable pageable);

    @Query("SELECT COUNT(p) FROM Person p WHERE " +
            "(:searchQuery IS NULL OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) AND " +
            "(:roleFilter IS NULL OR p.role = :roleFilter)")
    long countWithFilters(@Param("searchQuery") String searchQuery,
                          @Param("roleFilter") MovieRole roleFilter);

    @Query("SELECT p FROM Person p WHERE (p.fullName) IN (:names) AND p.role = :role")
    List<Person> findAllByNameAndRole(@Param("names") Set<String> names, @Param("role") MovieRole role);

}
