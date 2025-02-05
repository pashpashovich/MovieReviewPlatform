package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.RatingDto;
import by.innowise.moviereview.entity.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RateMapper {
    RatingDto toDto(Rating rating);
}
