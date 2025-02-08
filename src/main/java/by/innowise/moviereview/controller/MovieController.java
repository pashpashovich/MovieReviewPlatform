package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.util.PosterLoading;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "title", required = false) String query) {
        List<MovieDto> movies = (query == null)
                ? movieService.getMoviesWithPagination(page, pageSize)
                : movieService.filterMoviesWithPagination(query, page, pageSize);
        return ResponseEntity.ok(movies);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<MovieDto> createMovie(
            @RequestPart("movie") MovieDto movieDto,
            @RequestPart(value = "posterFile", required = false) MultipartFile posterFile) throws IOException {
        MovieDto createdMovie = movieService.createMovie(posterFile, movieDto);
        return ResponseEntity.ok(createdMovie);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable("id") Long id,
            @RequestPart("movie") MovieDto movieDto,
            @RequestPart(value = "posterFile", required = false) MultipartFile posterFile) throws IOException, EntityNotFoundException {

        MovieDto updatedMovie = movieService.updateMovie(id, posterFile, movieDto);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
