package by.innowise.moviereview.dto;

import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RatingDto {
    private User user;
    private Movie movie;
    private Integer rating;
}
