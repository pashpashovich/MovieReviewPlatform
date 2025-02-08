package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class WatchlistRequest {
    private Long userId;
    private Long movieId;
}
