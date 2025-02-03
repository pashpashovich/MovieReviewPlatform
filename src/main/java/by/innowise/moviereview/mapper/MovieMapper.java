package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.util.enums.MovieRole;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {MovieRole.class})
public interface MovieMapper {

    @Named("toListDto")
    @Mapping(target = "genres", source = "genres")
    @Mapping(target = "actors", expression = "java(mapPeopleByRole(movie.getPeople(), MovieRole.ACTOR))")
    @Mapping(target = "directors", expression = "java(mapPeopleByRole(movie.getPeople(), MovieRole.DIRECTOR))")
    @Mapping(target = "producers", expression = "java(mapPeopleByRole(movie.getPeople(), MovieRole.PRODUCER))")
    MovieDto toDto(Movie movie);

    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "producers", ignore = true)
    MovieDto toDtoForRecomendations(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "people", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "watchlist", ignore = true)
    Movie toEntityFromDto(MovieDto movieDto);


    @IterableMapping(qualifiedByName = "toListDto")
    List<MovieDto> toDtoList(List<Movie> movies);

    default Set<String> mapGenresToNames(Set<Genre> genres) {
        if (genres == null) return null;
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> mapPeopleByRole(Set<Person> people, MovieRole role) {
        if (people == null) return null;
        return people.stream()
                .filter(person -> person.getRole() == role)
                .map(Person::getFullName)
                .collect(Collectors.toSet());
    }
}
