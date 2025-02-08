package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.entity.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDto toDto(Review review);

    List<ReviewDto> toListDto(List<Review> review);

}
