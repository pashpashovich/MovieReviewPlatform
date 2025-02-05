package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.EntityCreateDto;
import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.service.GenreService;
import lombok.RequiredArgsConstructor;
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

import java.util.Map;

@RestController
@RequestMapping("/admin/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getGenres(
            @RequestParam(value = "search", required = false) String searchQuery,
            @RequestParam(value = "sort", defaultValue = "id") String sortField,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize) {

        Map<String, Object> result = genreService.getGenresWithFilters(searchQuery, sortField, page, pageSize);

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

    @DeleteMapping
    public ResponseEntity<Void> deleteGenre(@RequestParam("id") Long id) {
        genreService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
