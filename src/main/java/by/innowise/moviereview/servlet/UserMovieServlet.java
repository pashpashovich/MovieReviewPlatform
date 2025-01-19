package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.GenreService;
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
import java.util.List;

@WebServlet("/user/movies")
public class UserMovieServlet extends HttpServlet {
    private final MovieService movieService = MovieService.getInstance();
    private final GenreService genreService = GenreService.getInstance();
    private final RatingService ratingService = RatingService.getInstance();
    private final RecommendationService recommendationService = RecommendationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        int size = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : 9;
        HttpSession session = req.getSession();
        Long userId = session.getAttribute("user") != null ? ((UserDto) session.getAttribute("user")).getId() : null;

        String searchQuery = req.getParameter("searchQuery") != null ? req.getParameter("searchQuery") : "";
        String genreId = req.getParameter("genre") != null ? req.getParameter("genre") : "";
        String language = req.getParameter("language") != null ? req.getParameter("language") : "";
        String year = req.getParameter("year") != null ? req.getParameter("year") : "";
        String duration = req.getParameter("duration") != null ? req.getParameter("duration") : "";

        List<MovieDto> movies = movieService.filterMoviesWithPagination(searchQuery, genreId, language, year, duration, page, size);
        long totalMovies;
        if (searchQuery.isEmpty() && genreId.isEmpty() && language.isEmpty() && year.isEmpty() && duration.isEmpty()) {
            totalMovies = movieService.getTotalMoviesCount();
        } else {
            totalMovies = movies.size();
        }

        int totalPages = (int) Math.ceil((double) totalMovies / size);
        List<EntityDto> genres = genreService.findAll();

        List<MovieDto> recommendations = recommendationService.getRecommendationsForUser(userId);

        req.setAttribute("searchQuery", searchQuery);
        req.setAttribute("genre", genreId);
        req.setAttribute("language", language);
        req.setAttribute("year", year);
        req.setAttribute("duration", duration);

        req.setAttribute("movies", movies);
        req.setAttribute("genres", genres);
        req.setAttribute("recommendations", recommendations);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/WEB-INF/views/user/movieCards.jsp").forward(req, resp);
    }
}

