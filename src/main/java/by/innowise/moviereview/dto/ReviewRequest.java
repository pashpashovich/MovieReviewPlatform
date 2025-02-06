package by.innowise.moviereview.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long userId;
    private Long movieId;
    private String content;
    private int rating;
}
