package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieResponse {
    private Long userId;
    private List<MovieDto> movies;
    private int totalPages;
    private List<EntityDto> genres;
    private List<MovieDto> recommendations;
}
