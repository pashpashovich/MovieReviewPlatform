package by.innowise.moviereview.dto;

import lombok.Data;

@Data
public class MovieCreateDto {
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer duration;
    private String language;
    private String posterUrl;
}
