package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.MovieDetailsDto;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.facade.MovieDetailsFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/movies")
@RequiredArgsConstructor
public class MovieDetailsController {

    private final MovieDetailsFacade movieDetailsFacade;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieDetailsDto> getMovieDetails(@PathVariable("movieId") Long movieId,
                                                           @RequestParam("userId") Long userId) throws EntityNotFoundException {
        return ResponseEntity.ok(movieDetailsFacade.getMovieDetails(movieId, userId));
    }
}