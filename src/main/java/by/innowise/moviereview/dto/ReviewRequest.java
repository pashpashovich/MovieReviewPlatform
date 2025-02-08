package by.innowise.moviereview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ReviewRequest {
    private Long userId;
    private Long movieId;
    private String content;
    private int rating;
}
