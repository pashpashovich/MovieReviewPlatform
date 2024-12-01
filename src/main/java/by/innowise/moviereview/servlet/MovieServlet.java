package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.MovieCreateDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.mapper.MovieMapper;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import by.innowise.moviereview.repository.*;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.util.enums.MovieRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@WebServlet("/admin/movies")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50)
public class MovieServlet extends HttpServlet {
    private final MovieService movieService;
    private final GenreRepository genreRepository;
    private final PersonRepository personRepository;

    public MovieServlet() {
        MovieRepository movieRepository = new MovieRepositoryImpl();
        MovieMapper movieMapper = new MovieMapperImpl();
        this.genreRepository = new GenreRepositoryImpl();
        this.personRepository = new PersonRepositoryImpl();
        this.movieService = new MovieService(movieRepository, movieMapper, personRepository, genreRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MovieDto> movies = movieService.getAllMovies();
        req.setAttribute("movies", movies);
        req.setAttribute("genres", genreRepository.findAll());
        req.setAttribute("actors", personRepository.findAllByRole(MovieRole.ACTOR));
        req.setAttribute("directors", personRepository.findAllByRole(MovieRole.DIRECTOR));
        req.setAttribute("producers", personRepository.findAllByRole(MovieRole.PRODUCER));
        req.getRequestDispatcher("/WEB-INF/views/admin/movies.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        MovieCreateDto movieDto = extractMovieFromRequest(req);
        movieService.createMovie(movieDto);
        resp.sendRedirect(req.getContextPath() + "/admin/movies");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Long id = Long.valueOf(req.getParameter("id"));
        MovieCreateDto movieDto = extractMovieFromRequest(req);
        movieService.updateMovie(id, movieDto);
        resp.sendRedirect(req.getContextPath() + "/admin/movies");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        try {
            movieService.deleteMovie(id);
            resp.sendRedirect(req.getContextPath() + "/admin/movies");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка удаления фильма: " + e.getMessage());
        }
    }

    private MovieCreateDto extractMovieFromRequest(HttpServletRequest req) throws IOException, ServletException {
        MovieCreateDto movieDto = new MovieCreateDto();
        movieDto.setTitle(req.getParameter("title"));
        movieDto.setDescription(req.getParameter("description"));
        movieDto.setReleaseYear(Integer.parseInt(req.getParameter("releaseYear")));
        movieDto.setDuration(Integer.parseInt(req.getParameter("duration")));
        movieDto.setLanguage(req.getParameter("language"));
        Part posterPart = req.getPart("posterFile");
        if (posterPart != null && posterPart.getSize() > 0) {
            byte[] posterBytes = posterPart.getInputStream().readAllBytes();
            String posterBase64 = Base64.getEncoder().encodeToString(posterBytes);
            movieDto.setPosterBase64(posterBase64);
        }
        String[] genreIds = req.getParameterValues("genreIds");
        if (genreIds != null) {
            movieDto.setGenreIds(List.of(genreIds).stream().map(Long::valueOf).toList());
        }
        String[] actorIds = req.getParameterValues("actorIds");
        if (actorIds != null) {
            movieDto.setActorIds(List.of(actorIds).stream().map(Long::valueOf).toList());
        }
        String[] directorIds = req.getParameterValues("directorIds");
        if (directorIds != null) {
            movieDto.setDirectorIds(List.of(directorIds).stream().map(Long::valueOf).toList());
        }
        String[] producerIds = req.getParameterValues("producerIds");
        if (producerIds != null) {
            movieDto.setProducerIds(List.of(producerIds).stream().map(Long::valueOf).toList());
        }

        return movieDto;
    }
}
