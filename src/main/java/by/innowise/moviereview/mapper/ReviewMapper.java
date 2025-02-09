package by.innowise.moviereview.mapper;

import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "movieName", source = "movie.title")
    ReviewDto toDto(Review review);

    List<ReviewDto> toListDto(List<Review> review);

}
