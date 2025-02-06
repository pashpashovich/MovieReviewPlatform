package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.dto.ReviewRequest;
import by.innowise.moviereview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/movies")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<ReviewDto> addReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewDto reviewDto = reviewService.addReview(
                reviewRequest.getUserId(),
                reviewRequest.getMovieId(),
                reviewRequest.getContent(),
                reviewRequest.getRating()
        );
        return ResponseEntity.ok(reviewDto);
    }

}
