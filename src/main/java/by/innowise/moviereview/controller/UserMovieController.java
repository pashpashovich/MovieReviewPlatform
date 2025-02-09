package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.MovieFilterRequest;
import by.innowise.moviereview.dto.MovieResponse;
import by.innowise.moviereview.facade.UserMovieFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/movies")
@RequiredArgsConstructor
public class UserMovieController {
    private UserMovieFacade userMovieFacade;

    @GetMapping("/{userId}")
    public ResponseEntity<MovieResponse> getMovies(
            @PathVariable Long userId,
            @ModelAttribute MovieFilterRequest filterRequest) {
        MovieResponse response = userMovieFacade.getResponse(userId, filterRequest);
        return ResponseEntity.ok(response);
    }
}