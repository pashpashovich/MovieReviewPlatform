package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper()
public interface GenreMapper {

    EntityDto toDto(Genre genre);
    List<EntityDto> toListDto(List<Genre> genres);
    Genre toEntity(EntityDto entityDto);
}
