package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.MovieCreateDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.Person;
import by.innowise.moviereview.enums.MovieRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {MovieRole.class})
public interface MovieMapper {

    @Named("toListDto")
    @Mapping(target = "genres", source = "genres", qualifiedByName = "mapGenresToNames")
    @Mapping(target = "actors", source = "people", qualifiedByName = "mapPeopleToActors")
    @Mapping(target = "directors", source = "people", qualifiedByName = "mapPeopleToDirectors")
    @Mapping(target = "producers", source = "people", qualifiedByName = "mapPeopleToProducers")
    MovieDto toDto(Movie movie);

    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "producers", ignore = true)
    MovieDto toDtoForRecommendations(Movie movie);

    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "producers", ignore = true)
    List<MovieDto> toListDtoForRecommendations(List<Movie> movie);

    @Mapping(target = "avgRating", ignore = true)
    @Mapping(target = "posterBase64", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "people", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "watchlist", ignore = true)
    Movie toEntityFromDto(MovieCreateDto movieDto);


    @Named("mapGenresToNames")
    default Set<String> mapGenresToNames(Set<Genre> genres) {
        if (genres == null) return Collections.emptySet();
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toSet());
    }

    @Named("mapPeopleToActors")
    default Set<String> mapPeopleToActors(Set<Person> people) {
        return mapPeopleByRole(people, MovieRole.ACTOR);
    }

    @Named("mapPeopleToDirectors")
    default Set<String> mapPeopleToDirectors(Set<Person> people) {
        return mapPeopleByRole(people, MovieRole.DIRECTOR);
    }

    @Named("mapPeopleToProducers")
    default Set<String> mapPeopleToProducers(Set<Person> people) {
        return mapPeopleByRole(people, MovieRole.PRODUCER);
    }

    default Set<String> mapPeopleByRole(Set<Person> people, MovieRole role) {
        if (people == null) return Collections.emptySet();
        return people.stream()
                .filter(person -> person.getRole() == role)
                .map(Person::getFullName)
                .collect(Collectors.toSet());
    }
}
