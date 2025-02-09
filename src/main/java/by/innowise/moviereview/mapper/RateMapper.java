package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.RatingDto;
import by.innowise.moviereview.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RateMapper {
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "movieTitle", source = "movie.title")
    RatingDto toDto(Rating rating);
}
