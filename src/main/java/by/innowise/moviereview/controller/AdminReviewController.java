package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.ErrorResponseImpl;
import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.service.ReviewService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

//В контроллерах советую использовать анатации @Tag и @Operation
//для большей информативности в Swagger документации

@RestController
@RequestMapping("api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseImpl> handleEnumException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseImpl("Такого статуса не существует", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getPendingReviews() {
        List<ReviewDto> reviews = reviewService.findAllPendingReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReviewStatus(@PathVariable("reviewId") @NotNull Long reviewId, @RequestParam("status") @NotNull String status) {
        ReviewDto reviewDto = reviewService.updateReviewStatus(reviewId, status);
        return ResponseEntity.ok(reviewDto);
    }
}
