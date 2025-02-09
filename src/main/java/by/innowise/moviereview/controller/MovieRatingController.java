package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.ErrorResponseImpl;
import by.innowise.moviereview.dto.RateCreateDto;
import by.innowise.moviereview.dto.RatingDto;
import by.innowise.moviereview.dto.RatingUpdateRequest;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.exception.UserNotFoundException;
import by.innowise.moviereview.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/user/movies/rate")
@RequiredArgsConstructor
public class MovieRatingController {
    private final RatingService ratingService;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseImpl> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseImpl> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseImpl> handleUserNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @PostMapping()
    public ResponseEntity<RatingDto> rateMovie(@RequestBody RateCreateDto rateDto) {
        RatingDto ratingDto = ratingService.saveRating(rateDto);
        return ResponseEntity.ok(ratingDto);
    }

    @PatchMapping("/{rateId}")
    public ResponseEntity<RatingDto> updateRateMovie(@PathVariable("rateId") Long rateId, @RequestBody RatingUpdateRequest ratingUpdateRequest) {
        RatingDto ratingDto = ratingService.updateRating(rateId, ratingUpdateRequest);
        return ResponseEntity.ok(ratingDto);
    }
}


