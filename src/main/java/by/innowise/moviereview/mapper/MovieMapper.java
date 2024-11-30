package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.MovieCreateDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MovieMapper {
    MovieDto toDto(Movie movie);
    @Mapping(target = "id", ignore = true)
    Movie toEntityFromDto(MovieDto movieDto);
    @Mapping(target = "id", ignore = true)
    Movie toEntityFromCreateDto(MovieCreateDto movieDto);
}
