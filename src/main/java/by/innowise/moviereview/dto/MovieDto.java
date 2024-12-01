package by.innowise.moviereview.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer duration;
    private String language;
    private Double avgRating;
    private String posterBase64;
    private List<String> genres;
    private List<String> actors;
    private List<String> directors;
    private List<String> producers;
}
