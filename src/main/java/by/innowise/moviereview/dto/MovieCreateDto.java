package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
public class MovieCreateDto {
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer duration;
    private String language;
    private Set<String> genres;
    private Set<String> actors;
    private Set<String> directors;
    private Set<String> producers;
}
