package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import by.innowise.moviereview.repository.GenreRepositoryImpl;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.PersonRepositoryImpl;
import by.innowise.moviereview.repository.RatingRepositoryImpl;
import by.innowise.moviereview.repository.UserRepositoryImpl;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RatingService;
import by.innowise.moviereview.service.RecommendationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/user/movies")
public class UserMovieServlet extends HttpServlet {
    private final MovieService movieService;
    private final GenreRepositoryImpl genreRepository;
    private final RatingService ratingService;
    private final RecommendationService recommendationService;

    public UserMovieServlet() {
        this.genreRepository = new GenreRepositoryImpl();
        this.ratingService = new RatingService(new RatingRepositoryImpl(), new UserRepositoryImpl(), new MovieRepositoryImpl());
        this.movieService = new MovieService(new MovieRepositoryImpl(), new MovieMapperImpl(), new PersonRepositoryImpl(), genreRepository);
        this.recommendationService = new RecommendationService(new RatingRepositoryImpl(), new MovieRepositoryImpl(), new MovieMapperImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Long userId = session.getAttribute("user") != null ? ((UserDto) session.getAttribute("user")).getId() : null;

        String searchQuery = req.getParameter("searchQuery") != null ? req.getParameter("searchQuery") : "";
        String genreId = req.getParameter("genre") != null ? req.getParameter("genre") : "";
        String language = req.getParameter("language") != null ? req.getParameter("language") : "";
        String year = req.getParameter("year") != null ? req.getParameter("year") : "";
        String duration = req.getParameter("duration") != null ? req.getParameter("duration") : "";

        List<MovieDto> movies = movieService.filterMovies(searchQuery, genreId, language, year, duration);
        List<Genre> genres = genreRepository.findAll();

        Map<Long, Integer> userRatings = new HashMap<>();
        if (userId != null) {
            for (MovieDto movie : movies) {
                Integer rating = ratingService.getRatingByUserAndMovie(userId, movie.getId());
                userRatings.put(movie.getId(), rating);
            }
        }

        List<MovieDto> recommendations = recommendationService.getRecommendationsForUser(userId);

        req.setAttribute("searchQuery", searchQuery);
        req.setAttribute("genre", genreId);
        req.setAttribute("language", language);
        req.setAttribute("year", year);
        req.setAttribute("duration", duration);

        req.setAttribute("movies", movies);
        req.setAttribute("genres", genres);
        req.setAttribute("userRatings", userRatings);
        req.setAttribute("recommendations", recommendations);

        req.getRequestDispatcher("/WEB-INF/views/user/movieCards.jsp").forward(req, resp);
    }
}

