package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RateCreateDto {
    private Long userId;
    private Long movieId;
    private int rating;
}
