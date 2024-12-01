package by.innowise.moviereview.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieCreateDto {
    private String title;
    private String description;
    private Integer releaseYear;
    private Integer duration;
    private String language;
    private String posterBase64;
    private List<Long> genreIds;
    private List<Long> actorIds;
    private List<Long> directorIds;
    private List<Long> producerIds;
}
