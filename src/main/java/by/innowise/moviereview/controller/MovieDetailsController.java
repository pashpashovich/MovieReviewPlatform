package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.WatchlistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user/movies")
public class MovieDetailsController {

    private final RatingService ratingService;
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final WatchlistService watchlistService;

    public MovieDetailsController(RatingService ratingService,
                                  MovieService movieService,
                                  ReviewService reviewService,
                                  WatchlistService watchlistService) {
        this.ratingService = ratingService;
        this.movieService = movieService;
        this.reviewService = reviewService;
        this.watchlistService = watchlistService;
    }

    @GetMapping("/movie/{movieId}")
    public String getMovieDetails(@PathVariable("movieId") Long movieId,
                                  @RequestParam("userId") Long userId,
                                  Model model) {
        try {
            MovieDto movie = movieService.getMovieById(movieId);
            if (movie == null) {
                throw new IllegalArgumentException("Фильм с ID " + movieId + " не найден");
            }

            Double averageRating = ratingService.getAverageRatingForMovie(movieId);
            List<Review> approvedReviews = reviewService.findApprovedReviewsByMovieId(movieId);
            boolean isInList = watchlistService.isMovieInWatchlist(userId, movieId);
            Integer ratingByUserAndMovie = ratingService.getRatingByUserAndMovie(userId, movieId);

            model.addAttribute("userId", userId);
            model.addAttribute("movie", movie);
            model.addAttribute("averageRating", averageRating);
            model.addAttribute("reviews", approvedReviews);
            model.addAttribute("isInList", isInList);
            model.addAttribute("rating", ratingByUserAndMovie);

            return "user/movie-details";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error/404";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Произошла ошибка.");
            return "error/500";
        }
    }
}

