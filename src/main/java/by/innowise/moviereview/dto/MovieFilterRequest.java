package by.innowise.moviereview.dto;

import lombok.Data;

@Data
public class MovieFilterRequest {
    private int page;
    private int size;
    private String searchQuery;
    private String genreId;
    private String language;
    private String year;
    private String duration;
}
