package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.MovieCreateDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.enums.MovieRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(imports = {MovieRole.class})
public interface MovieMapper {

    @Mapping(target = "genres", source = "genres")
    @Mapping(target = "actors", expression = "java(mapPeopleByRole(movie.getPeople(), MovieRole.ACTOR))")
    @Mapping(target = "directors", expression = "java(mapPeopleByRole(movie.getPeople(), MovieRole.DIRECTOR))")
    @Mapping(target = "producers", expression = "java(mapPeopleByRole(movie.getPeople(), MovieRole.PRODUCER))")
    MovieDto toDto(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "people", ignore = true) // Люди обрабатываются отдельно
    @Mapping(target = "genres", ignore = true) // Жанры обрабатываются отдельно
    @Mapping(target = "createdAt", ignore = true) // Игнорируем поле
    @Mapping(target = "reviews", ignore = true) // Игнорируем поле
    @Mapping(target = "ratings", ignore = true) // Игнорируем поле
    @Mapping(target = "watchlist", ignore = true) // Игнорируем поле
    Movie toEntityFromDto(MovieDto movieDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "people", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "avgRating", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "watchlist", ignore = true)
    Movie toEntityFromCreateDto(MovieCreateDto movieDto);

    default List<String> mapGenresToNames(List<Genre> genres) {
        if (genres == null) return null;
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
    }

    default List<String> mapPeopleByRole(List<Person> people, MovieRole role) {
        if (people == null) return null;
        return people.stream()
                .filter(person -> person.getRole() == role)
                .map(Person::getFullName)
                .collect(Collectors.toList());
    }
}
