package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Review;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import by.innowise.moviereview.repository.GenreRepositoryImpl;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.PersonRepositoryImpl;
import by.innowise.moviereview.repository.RatingRepositoryImpl;
import by.innowise.moviereview.repository.UserRepositoryImpl;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.service.ReviewService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MovieDetailsServlet", urlPatterns = "/user/movies/*")
public class MovieDetailsServlet extends HttpServlet {
    private final MovieService movieService;
    private final MovieRepositoryImpl movieRepository;
    private final MovieMapper movieMapper;
    private final PersonRepositoryImpl personRepository;
    private final GenreRepositoryImpl genreRepository;
    private final RatingService ratingService;
    private final ReviewService reviewService;

    public MovieDetailsServlet() {
        this.reviewService = new ReviewService();
        this.personRepository = new PersonRepositoryImpl();
        this.movieMapper = new MovieMapperImpl();
        this.movieRepository = new MovieRepositoryImpl();
        this.genreRepository = new GenreRepositoryImpl();
        this.ratingService = new RatingService(new RatingRepositoryImpl(), new UserRepositoryImpl(), movieRepository);
        this.movieService = new MovieService(movieRepository, movieMapper, personRepository, genreRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String movieIdParam = req.getPathInfo().substring(1);
            if (movieIdParam == null || movieIdParam.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан ID фильма");
                return;
            }
            Long movieId = Long.valueOf(movieIdParam);
            MovieDto movie = movieService.findMovieById(movieId);
            if (movie == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Фильм с ID " + movieId + " не найден");
                return;
            }
            Double averageRating = ratingService.getAverageRatingForMovie(movieId);
            List<Review> approvedReviews = reviewService.findApprovedReviewsByMovieId(movieId);
            req.setAttribute("movie", movie);
            req.setAttribute("averageRating", averageRating);
            req.setAttribute("reviews", approvedReviews);

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
