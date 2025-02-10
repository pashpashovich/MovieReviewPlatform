package by.innowise.moviereview.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class WatchlistRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long movieId;
}
