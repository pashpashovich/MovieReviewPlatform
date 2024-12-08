package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WatchlistDto {
    private Long movieId;
    private String movieTitle;
    private String posterBase64;
    private LocalDateTime addedAt;
}
