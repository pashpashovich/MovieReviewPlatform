package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.EntityCreateDto;
import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.ErrorResponseImpl;
import by.innowise.moviereview.dto.GenreFilterDto;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.service.GenreService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseImpl> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponseImpl> handleEntityExistsException(EntityExistsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseImpl(ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getGenres(@ModelAttribute GenreFilterDto filter) {
        List<String> allowedSortFields = List.of("id", "name");
        if (!allowedSortFields.contains(filter.getSort())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid sort field"));
        }

        Map<String, Object> result = genreService.getGenresWithFilters(filter);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<EntityDto> addGenre(@RequestBody EntityCreateDto entityCreateDto) {
        EntityDto entityDto1 = genreService.save(entityCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityDto1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityDto> updateGenre(@PathVariable("id") Long id, @RequestBody EntityCreateDto entityCreateDto) {
        EntityDto updated = genreService.update(id, entityCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable("id") Long id) {
        genreService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
