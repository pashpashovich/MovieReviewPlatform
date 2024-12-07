package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import by.innowise.moviereview.repository.GenreRepositoryImpl;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.PersonRepositoryImpl;
import by.innowise.moviereview.service.MovieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/movies")
public class UserMovieServlet extends HttpServlet {
    private final MovieService movieService;
    private final GenreRepositoryImpl genreRepository;
    private final PersonRepositoryImpl personRepository;

    public UserMovieServlet() {
        MovieRepositoryImpl movieRepository = new MovieRepositoryImpl();
        MovieMapper movieMapper = new MovieMapperImpl();
        this.genreRepository = new GenreRepositoryImpl();
        this.personRepository = new PersonRepositoryImpl();
        this.movieService = new MovieService(movieRepository, movieMapper, personRepository, genreRepository);
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
        String searchQuery = req.getParameter("searchQuery");
        String genreId = req.getParameter("genre");
        String language = req.getParameter("language");
        String year = req.getParameter("year");
        String duration = req.getParameter("duration");
        List<MovieDto> movies = movieService.filterMovies(searchQuery, genreId, language, year, duration);
        List<Genre> genres = genreRepository.findAll();
        req.setAttribute("movies", movies);
        req.setAttribute("genres", genres);
        req.getRequestDispatcher("/WEB-INF/views/user/movieCards.jsp").forward(req, resp);
    }
}
