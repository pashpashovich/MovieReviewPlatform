package by.innowise.moviereview.servlet;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.mapper.GenreMapperImpl;
import by.innowise.moviereview.mapper.MovieMapperImpl;
import by.innowise.moviereview.mapper.PersonMapperImpl;
import by.innowise.moviereview.repository.GenreRepositoryImpl;
import by.innowise.moviereview.repository.MovieRepositoryImpl;
import by.innowise.moviereview.repository.PersonRepositoryImpl;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.PersonService;
import by.innowise.moviereview.util.enums.MovieRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/movies")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50)
public class MovieServlet extends HttpServlet {
    private final MovieService movieService;
    private final GenreService genreService;
    private final PersonService personService;

    public MovieServlet() {
        this.movieService = new MovieService(new MovieRepositoryImpl(), new MovieMapperImpl(), new PersonRepositoryImpl(), new GenreRepositoryImpl());
        this.genreService=new GenreService(new GenreRepositoryImpl(),new GenreMapperImpl());
        this.personService=new PersonService(new PersonRepositoryImpl(), new PersonMapperImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MovieDto> movies = movieService.getAllMovies();
        req.setAttribute("movies", movies);
        req.setAttribute("genres", genreService.findAll());
        req.setAttribute("actors", personService.getAllPeopleByRole(MovieRole.ACTOR));
        req.setAttribute("directors", personService.getAllPeopleByRole(MovieRole.DIRECTOR));
        req.setAttribute("producers", personService.getAllPeopleByRole(MovieRole.PRODUCER));
        req.getRequestDispatcher("/WEB-INF/views/admin/movies.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        MovieDto movieDto = extractMovieFromRequest(req);
        movieService.createMovie(movieDto);
        resp.sendRedirect(req.getContextPath() + "/admin/movies");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Long id = Long.valueOf(req.getParameter("id"));
        MovieDto movieDto = extractMovieFromRequest(req);
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

    private MovieDto extractMovieFromRequest(HttpServletRequest req) throws IOException, ServletException {
        MovieDto movieDto = new MovieDto();
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
        String[] genres = req.getParameterValues("genres");
        if (genres != null) {
            movieDto.setGenres(Arrays.stream(genres).collect(Collectors.toSet()));
        }
        String[] actors = req.getParameterValues("actors");
        if (actors != null) {
            movieDto.setActors(Arrays.stream(actors).collect(Collectors.toSet()));
        }
        String[] directors = req.getParameterValues("directors");
        if (directors != null) {
            movieDto.setDirectors(Arrays.stream(directors).collect(Collectors.toSet()));
        }
        String[] producers = req.getParameterValues("producers");
        if (producers != null) {
            movieDto.setProducers(Arrays.stream(producers).collect(Collectors.toSet()));
        }
        return movieDto;
    }
}
