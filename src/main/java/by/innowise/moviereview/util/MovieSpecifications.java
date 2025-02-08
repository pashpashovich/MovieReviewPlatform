package by.innowise.moviereview.util;

import by.innowise.moviereview.entity.Movie;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;


@UtilityClass
public class MovieSpecifications {

    public static Specification<Movie> withFilters(String searchQuery) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();
            if (searchQuery != null && !searchQuery.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + searchQuery.toLowerCase() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Movie> withFilters(String searchQuery, String genreId, String language, String year, String duration) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            if (searchQuery != null && !searchQuery.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + searchQuery.toLowerCase() + "%"));
            }

            if (genreId != null && !genreId.isEmpty()) {
                predicates.add(criteriaBuilder.isTrue(
                        root.join("genres").get("id").in(genreId)
                ));
            }

            if (language != null && !language.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("language"), language));
            }

            if (year != null && !year.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("releaseYear"), Integer.valueOf(year)));
            }

            if (duration != null && !duration.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("duration"), Integer.valueOf(duration)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
