package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class MovieDetailsDto {
    private Long userId;
    private MovieDto movie;
    private Double averageRating;
    private List<ReviewDto> reviews;
    private boolean isInList;
    private Integer userRating;
}

