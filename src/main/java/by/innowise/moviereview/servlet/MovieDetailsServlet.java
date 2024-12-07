package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.MovieDto;
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

@WebServlet(name = "MovieDetailsServlet", urlPatterns = "/user/movies/*")
public class MovieDetailsServlet extends HttpServlet {
    private final MovieService movieService;
    private final MovieRepositoryImpl movieRepository;
    private final MovieMapper movieMapper;
    private final PersonRepositoryImpl personRepository;
    private final GenreRepositoryImpl genreRepository;

    public MovieDetailsServlet() {
        this.personRepository=new PersonRepositoryImpl();
        this.movieMapper=new MovieMapperImpl();
        this.movieRepository=new MovieRepositoryImpl();
        this.genreRepository=new GenreRepositoryImpl();
        this.movieService = new MovieService(movieRepository,movieMapper,personRepository,genreRepository);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String movieIdParam = req.getPathInfo().substring(1);
        if (movieIdParam == null || movieIdParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан ID фильма");
            return;
        }
        try {
            Long movieId = Long.valueOf(movieIdParam);
            MovieDto movie = movieService.findMovieById(movieId);
            if (movie == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Фильм с ID " + movieId + " не найден");
                return;
            }
            req.setAttribute("movie", movie);
            req.getRequestDispatcher("/WEB-INF/views/user/movie-details.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный ID фильма");
        }
    }
}
