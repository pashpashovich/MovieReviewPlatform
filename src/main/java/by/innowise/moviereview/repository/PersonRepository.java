package by.innowise.moviereview.repository;

import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.enums.MovieRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByRole(MovieRole role);

    Page<Person> findByRoleAndNameContainingIgnoreCase(MovieRole role, String name, Pageable pageable);

    Page<Person> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p FROM Person p WHERE LOWER(p.fullName) IN :names AND p.role = :role")
    Set<Person> findAllByNameAndRole(Set<String> names, String role);

    @Query("SELECT p FROM Person p " +
            "WHERE (:searchQuery IS NULL OR LOWER(p.fullName) LIKE CONCAT('%', LOWER(:searchQuery), '%')) " +
            "AND (:roleFilter IS NULL OR p.role = :roleFilter)")
    Page<Person> findPeopleWithFilters(String searchQuery, String roleFilter, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Person p " +
            "WHERE (:searchQuery IS NULL OR LOWER(p.fullName) LIKE CONCAT('%', LOWER(:searchQuery), '%')) " +
            "AND (:roleFilter IS NULL OR p.role = :roleFilter)")
    long countPeople(String searchQuery, String roleFilter);
}
