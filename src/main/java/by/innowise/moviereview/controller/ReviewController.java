package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/movies")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<ReviewDto> addReview(
            @RequestParam("userId") Long userId,
            @RequestParam("movieId") Long movieId,
            @RequestParam("content") String content,
            @RequestParam("rating") int rating) {
        ReviewDto reviewDto = reviewService.addReview(userId, movieId, content, rating);
        return ResponseEntity.ok(reviewDto);
    }
}
