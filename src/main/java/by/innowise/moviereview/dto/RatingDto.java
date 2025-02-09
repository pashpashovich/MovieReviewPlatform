package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RatingDto {
    private Long id;
    private String username;
    private String movieTitle;
    private Integer rating;
}
