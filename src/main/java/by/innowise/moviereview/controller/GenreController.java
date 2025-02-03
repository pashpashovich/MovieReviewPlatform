package by.innowise.moviereview.controller;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.service.GenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/admin/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public String getGenres(
            @RequestParam(value = "search", required = false) String searchQuery,
            @RequestParam(value = "sort", defaultValue = "id") String sortField,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            Model model) {

        Map<String, Object> result = genreService.getGenresWithFilters(searchQuery, sortField, page, pageSize);
        model.addAttribute("entities", result.get("genres"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("currentPage", result.get("currentPage"));

        return "admin/entity-management";
    }

    @PostMapping
    public String addGenre(@RequestParam("name") String name) {
        EntityDto entityDto = EntityDto.builder()
                .name(name)
                .build();
        genreService.save(entityDto);
        return "redirect:/admin/genres";
    }

    @PutMapping
    public String updateGenre(@RequestParam("id") Long id, @RequestParam("name") String name) {
        genreService.update(new EntityDto(id, name));
        return "redirect:/admin/genres";
    }

    @DeleteMapping
    public String deleteGenre(@RequestParam("id") Long id) {
        genreService.delete(id);
        return "redirect:/admin/genres";
    }
}
