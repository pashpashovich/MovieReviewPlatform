package by.innowise.moviereview.dto;


import lombok.Data;

@Data
public class WatchlistRequest {
    private Long userId;
    private Long movieId;
}
