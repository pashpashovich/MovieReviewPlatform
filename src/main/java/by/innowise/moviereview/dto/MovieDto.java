package by.innowise.moviereview.dto;

import lombok.Data;

import java.util.Set;

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
    private Set<String> genres;
    private Set<String> actors;
    private Set<String> directors;
    private Set<String> producers;
}
