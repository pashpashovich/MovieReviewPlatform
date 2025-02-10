package by.innowise.moviereview.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ReviewRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long movieId;
    @NotEmpty
    @NotNull
    private String content;
    @NotNull
    @Min(0)
    @Max(5)
    private int rating;
}
