package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDto toDto(Review review);
}
