package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.ErrorResponseImpl;
import by.innowise.moviereview.dto.ReviewDto;
import by.innowise.moviereview.dto.ReviewRequest;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("api/user/movies")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseImpl> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @PostMapping("/review")
    public ResponseEntity<ReviewDto> addReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewDto reviewDto = reviewService.addReview(reviewRequest);
        return ResponseEntity.ok(reviewDto);
    }

}
