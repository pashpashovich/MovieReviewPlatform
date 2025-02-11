package by.innowise.moviereview.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PersonFilter {
    @NotNull
    @Builder.Default
    private int page = 0;
    @NotNull
    @Builder.Default
    private int size = 10;
    @NotNull
    private String search;
    @NotNull
    private String role;
}
