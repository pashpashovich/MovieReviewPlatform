package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.dto.PersonDto;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.PersonService;
import by.innowise.moviereview.util.enums.MovieRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/movies")
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final PersonService personService;

    @Autowired
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
        if (query == null) {
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
        processPosterFile(movieDto, posterFile);
        movieService.createMovie(movieDto);
        return "redirect:/admin/movies";
    }

    @PutMapping("/{id}")
    public String updateMovie(@PathVariable Long id,
                              @ModelAttribute MovieDto movieDto,
                              @RequestParam("posterFile") MultipartFile posterFile) throws IOException, EntityNotFoundException {
        processPosterFile(movieDto, posterFile);
        movieService.updateMovie(id, movieDto);
        return "redirect:/admin/movies";
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return "redirect:/admin/movies";
        } catch (Exception e) {
            throw new RuntimeException("Ошибка удаления фильма: " + e.getMessage(), e);
        }
    }

    private void processPosterFile(MovieDto movieDto, MultipartFile posterFile) throws IOException {
        if (posterFile != null && !posterFile.isEmpty()) {
            byte[] posterBytes = posterFile.getBytes();
            String posterBase64 = Base64.getEncoder().encodeToString(posterBytes);
            movieDto.setPosterBase64(posterBase64);
        } else {
            Path defaultPosterPath = Paths.get("src/main/resources/static/images/default-poster.png");
            byte[] defaultPosterBytes = Files.readAllBytes(defaultPosterPath);
            String defaultPosterBase64 = Base64.getEncoder().encodeToString(defaultPosterBytes);
            movieDto.setPosterBase64(defaultPosterBase64);
        }
    }

    @ModelAttribute("genres")
    public List<String> populateGenres() {
        return genreService.findAll().stream()
                .map(EntityDto::getName)
                .toList();
    }

    @ModelAttribute("actors")
    public Set<String> populateActors() {
        return personService.getAllPeopleByRole(MovieRole.ACTOR).stream()
                .map(PersonDto::getFullName)
                .collect(Collectors.toSet());
    }

    @ModelAttribute("directors")
    public Set<String> populateDirectors() {
        return personService.getAllPeopleByRole(MovieRole.DIRECTOR).stream()
                .map(PersonDto::getFullName)
                .collect(Collectors.toSet());
    }

    @ModelAttribute("producers")
    public Set<String> populateProducers() {
        return personService.getAllPeopleByRole(MovieRole.PRODUCER).stream()
                .map(PersonDto::getFullName)
                .collect(Collectors.toSet());
    }
}
