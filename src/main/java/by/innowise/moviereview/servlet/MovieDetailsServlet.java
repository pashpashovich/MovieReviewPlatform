package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.service.ReviewService;
import by.innowise.moviereview.service.WatchlistService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MovieDetailsServlet", urlPatterns = "/user/movies/*")
public class MovieDetailsServlet extends HttpServlet {
    private final RatingService ratingService = RatingService.getInstance();
    private final MovieService movieService = MovieService.getInstance();
    private final ReviewService reviewService = ReviewService.getInstance();
    private final WatchlistService watchlistService = WatchlistService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String movieIdParam = req.getPathInfo().substring(1);
            if (movieIdParam == null || movieIdParam.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан ID фильма");
                return;
            }
            Long movieId = Long.valueOf(movieIdParam);
            MovieDto movie = movieService.getMovieById(movieId);
            if (movie == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Фильм с ID " + movieId + " не найден");
                return;
            }
            Double averageRating = ratingService.getAverageRatingForMovie(movieId);
            List<Review> approvedReviews = reviewService.findApprovedReviewsByMovieId(movieId);
            HttpSession session = req.getSession(false);
            UserDto userDto = (UserDto) session.getAttribute("user");
            boolean isInList = watchlistService.isMovieInWatchlist(userDto.getId(), movieId);
            req.setAttribute("movie", movie);
            req.setAttribute("averageRating", averageRating);
            req.setAttribute("reviews", approvedReviews);
            req.setAttribute("isInList", isInList);
            req.getRequestDispatcher("/WEB-INF/views/user/movie-details.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Произошла ошибка.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
