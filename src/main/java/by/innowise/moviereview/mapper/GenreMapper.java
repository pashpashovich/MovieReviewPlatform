package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.EntityCreateDto;
import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    EntityDto toDto(Genre genre);

    List<EntityDto> toListDto(List<Genre> genres);

    @Mapping(target = "movies", ignore = true)
    Genre toEntity(EntityDto entityDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movies", ignore = true)
    Genre toCreateEntity(EntityCreateDto entityCreateDto);
}
