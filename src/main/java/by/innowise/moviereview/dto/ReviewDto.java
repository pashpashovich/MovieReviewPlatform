package by.innowise.moviereview.dto;

import by.innowise.moviereview.enums.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class ReviewDto {

    private Long id;

    private String username;

    private String movieName;

    private String content;

    private Integer rating;

    private ReviewStatus status;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
