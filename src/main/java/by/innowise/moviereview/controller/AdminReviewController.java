package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> getPendingReviews() {
        List<Review> reviews = reviewService.findAllPendingReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReviewStatus(@PathVariable("reviewId") Long reviewId, @RequestParam("status") String status) {
        ReviewDto reviewDto = reviewService.updateReviewStatus(reviewId, status);
        return ResponseEntity.ok(reviewDto);
    }
}
