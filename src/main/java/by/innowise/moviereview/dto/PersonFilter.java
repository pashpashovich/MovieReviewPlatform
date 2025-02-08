package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PersonFilter {
    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;
    private String search;
    private String role;
}
