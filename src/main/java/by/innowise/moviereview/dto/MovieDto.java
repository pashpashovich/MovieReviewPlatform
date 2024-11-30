package by.innowise.moviereview.dto;

import lombok.Data;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer duration;
    private String language;
    private Double avgRating;
}
