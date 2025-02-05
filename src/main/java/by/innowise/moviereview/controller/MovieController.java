package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.MovieDto;
import by.innowise.moviereview.exception.EntityNotFoundException;
import by.innowise.moviereview.service.GenreService;
import by.innowise.moviereview.service.MovieService;
import by.innowise.moviereview.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final PersonService personService;
    private final StandardServletMultipartResolver multipartResolver;


    @GetMapping
    public ResponseEntity<List<MovieDto>> getMovies(@RequestParam(value = "page", defaultValue = "1") int page,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                    @RequestParam(value = "title", required = false) String query) {
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

        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) throws IOException {
//        MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(request);
//        MultipartFile posterFile = multipartRequest.getFile("posterFile");
//        processPosterFile(movieDto, posterFile);
        MovieDto movie = movieService.createMovie(movieDto);
        return ResponseEntity.ok(movie);
    }

    @PutMapping("/{id}")
    public String updateMovie(@PathVariable("id") Long id,
                              @RequestBody MovieDto movieDto) throws IOException, EntityNotFoundException {
//        MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(request);
//        MultipartFile posterFile = multipartRequest.getFile("posterFile");
//        processPosterFile(movieDto, posterFile);
        movieService.updateMovie(id, movieDto);
        return "redirect:/admin/movies";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable("id") Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

//    private void processPosterFile(MovieDto movieDto, MultipartFile posterFile) throws IOException {
//        if (posterFile != null && !posterFile.isEmpty()) {
//            byte[] posterBytes = posterFile.getBytes();
//            String posterBase64 = Base64.getEncoder().encodeToString(posterBytes);
//            movieDto.setPosterBase64(posterBase64);
//        } else {
//            ClassPathResource defaultPosterResource = new ClassPathResource("static/images/default-poster.png");
//            try (InputStream inputStream = defaultPosterResource.getInputStream()) {
//                byte[] defaultPosterBytes = inputStream.readAllBytes();
//                String defaultPosterBase64 = Base64.getEncoder().encodeToString(defaultPosterBytes);
//                movieDto.setPosterBase64(defaultPosterBase64);
//            }
//        }
//    }

}