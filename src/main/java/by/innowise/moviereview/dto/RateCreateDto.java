package by.innowise.moviereview.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateCreateDto {
    private Long userId;
    private Long movieId;
    private int rating;
}
