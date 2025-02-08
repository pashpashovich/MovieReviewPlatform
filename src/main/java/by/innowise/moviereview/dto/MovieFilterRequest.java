package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class MovieFilterRequest {
    private int page;
    private int size;
    private String searchQuery;
    private String genreId;
    private String language;
    private String year;
    private String duration;
}
