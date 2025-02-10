package by.innowise.moviereview.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
public class MovieCreateDto {
    @NotNull
    @Size(min = 1, max = 50)
    private String title;
    @NotNull
    @Size(min = 5, max = 150)
    private String description;
    @NotNull
    @Min(1895)
    @Max(2030)
    private Integer releaseYear;
    @NotNull
    @Min(10)
    @Max(600)
    private Integer duration;
    @NotNull
    private String language;
    @NotNull
    private Set<String> genres;
    @NotNull
    private Set<String> actors;
    @NotNull
    private Set<String> directors;
    @NotNull
    private Set<String> producers;
}
