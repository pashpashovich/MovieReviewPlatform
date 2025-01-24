package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.PersonService;
import by.innowise.moviereview.util.enums.MovieRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/movies")
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final PersonService personService;

    public MovieController(MovieService movieService, GenreService genreService, PersonService personService) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.personService = personService;
    }

    @GetMapping
    public String getMovies(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                            @RequestParam(value = "title", required = false) String query,
                            Model model) {
        List<MovieDto> movies;
        long totalMovies;

        if (query == null || query.isEmpty()) {
            movies = movieService.getMoviesWithPagination(page, pageSize);
            totalMovies = movieService.getTotalMoviesCount();
        } else {
            movies = movieService.filterMoviesWithPagination(query, null, null, null, null, page, pageSize);
            totalMovies = movies.size();
        }

        int totalPages = (int) Math.ceil((double) totalMovies / pageSize);

        model.addAttribute("movies", movies);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("actors", personService.getAllPeopleByRole(MovieRole.ACTOR));
        model.addAttribute("directors", personService.getAllPeopleByRole(MovieRole.DIRECTOR));
        model.addAttribute("producers", personService.getAllPeopleByRole(MovieRole.PRODUCER));

        return "admin/movies";
    }

    @PostMapping
    public String createMovie(@ModelAttribute MovieDto movieDto,
                              @RequestParam("posterFile") MultipartFile posterFile) throws IOException {
        processPoster(movieDto, posterFile);
        movieService.createMovie(movieDto);
        return "redirect:/admin/movies";
    }

    @PutMapping
    public String updateMovie(@RequestParam("id") Long id,
                              @ModelAttribute MovieDto movieDto,
                              @RequestParam("posterFile") MultipartFile posterFile) throws IOException, EntityNotFoundException {
        processPoster(movieDto, posterFile);
        movieService.updateMovie(id, movieDto);
        return "redirect:/admin/movies";
    }

    @DeleteMapping
    public String deleteMovie(@RequestParam("id") Long id) {
        try {
            movieService.deleteMovie(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления фильма: " + e.getMessage(), e);
        }
        return "redirect:/admin/movies";
    }

    private void processPoster(MovieDto movieDto, MultipartFile posterFile) throws IOException {
        if (posterFile != null && !posterFile.isEmpty()) {
            byte[] posterBytes = posterFile.getBytes();
            String posterBase64 = Base64.getEncoder().encodeToString(posterBytes);
            movieDto.setPosterBase64(posterBase64);
        } else {
            movieDto.setPosterBase64(null);
        }
    }

    @ModelAttribute("movieDto")
    public MovieDto getMovieDto(@RequestParam(value = "genres", required = false) String[] genres,
                                @RequestParam(value = "actors", required = false) String[] actors,
                                @RequestParam(value = "directors", required = false) String[] directors,
                                @RequestParam(value = "producers", required = false) String[] producers) {
        MovieDto movieDto = new MovieDto();
        if (genres != null) {
            movieDto.setGenres(Arrays.stream(genres).collect(Collectors.toSet()));
        }
        if (actors != null) {
            movieDto.setActors(Arrays.stream(actors).collect(Collectors.toSet()));
        }
        if (directors != null) {
            movieDto.setDirectors(Arrays.stream(directors).collect(Collectors.toSet()));
        }
        if (producers != null) {
            movieDto.setProducers(Arrays.stream(producers).collect(Collectors.toSet()));
        }
        return movieDto;
    }
}
