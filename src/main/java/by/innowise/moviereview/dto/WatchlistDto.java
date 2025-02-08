package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class WatchlistDto {
    private Long movieId;
    private String movieTitle;
    private String posterBase64;
    private LocalDateTime addedAt;
}
