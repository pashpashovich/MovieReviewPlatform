package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class GenreFilterDto {
    private String search;
    private String sort;
    private int page;
    private int size;
}

