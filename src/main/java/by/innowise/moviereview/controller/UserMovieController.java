package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.UserDto;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.RecommendationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user/movies")
public class UserMovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final RecommendationService recommendationService;

    public UserMovieController(MovieService movieService, GenreService genreService, RecommendationService recommendationService) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public String getMovies(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "9") int size,
                            @RequestParam(value = "searchQuery", defaultValue = "") String searchQuery,
                            @RequestParam(value = "genre", defaultValue = "") String genreId,
                            @RequestParam(value = "language", defaultValue = "") String language,
                            @RequestParam(value = "year", defaultValue = "") String year,
                            @RequestParam(value = "duration", defaultValue = "") String duration,
                            HttpSession session, Model model) {

        Long userId = (session.getAttribute("user") != null) ? ((UserDto) session.getAttribute("user")).getId() : null;

        List<MovieDto> movies = movieService.filterMoviesWithPagination(searchQuery, genreId, language, year, duration, page, size);
        long totalMovies = (searchQuery.isEmpty() && genreId.isEmpty() && language.isEmpty() && year.isEmpty() && duration.isEmpty()) ?
                movieService.getTotalMoviesCount() : movies.size();

        int totalPages = (int) Math.ceil((double) totalMovies / size);
        List<EntityDto> genres = genreService.findAll();

        List<MovieDto> recommendations = recommendationService.getRecommendationsForUser(userId);

        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("genre", genreId);
        model.addAttribute("language", language);
        model.addAttribute("year", year);
        model.addAttribute("duration", duration);

        model.addAttribute("movies", movies);
        model.addAttribute("genres", genres);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "user/movieCards";
    }
}
