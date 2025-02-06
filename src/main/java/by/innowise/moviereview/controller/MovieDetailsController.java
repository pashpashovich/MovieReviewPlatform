package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/movies")
@RequiredArgsConstructor
public class MovieDetailsController {

    private final RatingService ratingService;
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final WatchlistService watchlistService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Map<String, Object>> getMovieDetails(@PathVariable("movieId") Long movieId,
                                                               @RequestParam("userId") Long userId) throws EntityNotFoundException {
        MovieDto movie = movieService.getMovieById(movieId);
        Double averageRating = ratingService.getAverageRatingForMovie(movieId);
        List<Review> approvedReviews = reviewService.findApprovedReviewsByMovieId(movieId);
        boolean isInList = watchlistService.isMovieInWatchlist(userId, movieId);
        Integer ratingByUserAndMovie = ratingService.getRatingByUserAndMovie(userId, movieId);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("movie", movie);
        response.put("averageRating", averageRating);
        response.put("reviews", approvedReviews);
        response.put("isInList", isInList);
        response.put("rating", ratingByUserAndMovie);

        return ResponseEntity.ok(response);
    }
}