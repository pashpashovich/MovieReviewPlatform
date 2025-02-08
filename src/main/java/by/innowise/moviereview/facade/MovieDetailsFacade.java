package by.innowise.moviereview.facade;

import by.innowise.moviereview.dto.MovieDetailsDto;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieDetailsFacade {

    private final RatingService ratingService;
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final WatchlistService watchlistService;

    public MovieDetailsDto getMovieDetails(Long movieId, Long userId) throws EntityNotFoundException {
        return MovieDetailsDto.builder()
                .userId(userId)
                .movie(movieService.getMovieById(movieId))
                .averageRating(ratingService.getAverageRatingForMovie(movieId))
                .reviews(reviewService.findApprovedReviewsByMovieId(movieId))
                .isInList(watchlistService.isMovieInWatchlist(userId, movieId))
                .userRating(ratingService.getRatingByUserAndMovie(userId, movieId))
                .build();
    }
}
