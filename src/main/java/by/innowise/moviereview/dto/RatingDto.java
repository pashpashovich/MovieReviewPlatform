package by.innowise.moviereview.dto;

import by.innowise.moviereview.entity.Movie;
import by.innowise.moviereview.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDto {
    private User user;
    private Movie movie;
    private Integer rating;
}
