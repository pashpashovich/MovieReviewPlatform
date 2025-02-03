package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class WatchlistDto {
    private Long movieId;
    private String movieTitle;
    private String posterBase64;
    private LocalDateTime addedAt;
}
