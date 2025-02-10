package by.innowise.moviereview.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class GenreFilterDto {
    @NotNull
    private String search;
    @NotNull
    private String sort;
    @NotNull
    private int page;
    @NotNull
    private int size;
}

