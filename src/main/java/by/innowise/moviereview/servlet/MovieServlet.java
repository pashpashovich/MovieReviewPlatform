package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.MovieCreateDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import by.innowise.moviereview.repository.MovieRepository;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.service.MovieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
@WebServlet("/admin/movies/*")
public class MovieServlet extends HttpServlet {
    private final MovieService movieService;

    public MovieServlet() {
        MovieRepository movieRepository = new MovieRepositoryImpl();
        MovieMapper movieMapper = new MovieMapperImpl();
        this.movieService = new MovieService(movieRepository, movieMapper);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
            List<MovieDto> movies = movieService.getAllMovies();
            req.setAttribute("movies", movies);
            req.getRequestDispatcher("/WEB-INF/views/admin/movies.jsp").forward(req, resp);
        } else if (pathInfo.startsWith("/edit")) {
            Long id = Long.valueOf(req.getParameter("id"));
            MovieDto movie = movieService.getMovieById(id);
            req.setAttribute("movie", movie);
            req.getRequestDispatcher("/WEB-INF/views/admin/movie-edit.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Страница не найдена");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MovieCreateDto movieCreateDTO = new MovieCreateDto();
        movieCreateDTO.setTitle(req.getParameter("title"));
        movieCreateDTO.setDescription(req.getParameter("description"));
        movieCreateDTO.setReleaseYear(Integer.parseInt(req.getParameter("releaseYear")));
        movieCreateDTO.setDuration(Integer.parseInt(req.getParameter("duration")));
        movieCreateDTO.setLanguage(req.getParameter("language"));
        movieCreateDTO.setPosterUrl(req.getParameter("posterUrl"));

        movieService.createMovie(movieCreateDTO);

        resp.sendRedirect(req.getContextPath() + "/admin/movies");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        MovieCreateDto movieCreateDTO = new MovieCreateDto();
        movieCreateDTO.setTitle(req.getParameter("title"));
        movieCreateDTO.setDescription(req.getParameter("description"));
        movieCreateDTO.setReleaseYear(Integer.parseInt(req.getParameter("releaseYear")));
        movieCreateDTO.setDuration(Integer.parseInt(req.getParameter("duration")));
        movieCreateDTO.setLanguage(req.getParameter("language"));
        movieCreateDTO.setPosterUrl(req.getParameter("posterUrl"));

        movieService.updateMovie(id, movieCreateDTO);

        resp.sendRedirect(req.getContextPath() + "/admin/movies");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getPathInfo().substring(1));
        movieService.deleteMovie(id);

        resp.sendRedirect(req.getContextPath() + "/admin/movies");
    }
}
