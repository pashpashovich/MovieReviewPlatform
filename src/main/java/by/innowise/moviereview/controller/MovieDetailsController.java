package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.ErrorResponseImpl;
import by.innowise.moviereview.dto.MovieDetailsDto;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.facade.MovieDetailsFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/user/movies")
@RequiredArgsConstructor
public class MovieDetailsController {

    private final MovieDetailsFacade movieDetailsFacade;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseImpl> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieDetailsDto> getMovieDetails(@PathVariable("movieId") Long movieId,
                                                           @RequestParam("userId") Long userId) throws EntityNotFoundException {
        return ResponseEntity.ok(movieDetailsFacade.getMovieDetails(movieId, userId));
    }
}