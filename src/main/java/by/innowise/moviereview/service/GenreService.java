package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.exception.NotFoundException;
import by.innowise.moviereview.mapper.GenreMapper;
import by.innowise.moviereview.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<EntityDto> findAll() {
        return genreMapper.toListDto(genreRepository.findAll());
    }

    public Map<String, Object> getGenresWithFilters(String searchQuery, String sortField, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortField != null ? sortField : "id"));
        Page<Genre> genrePage = genreRepository.findAllWithFilters(searchQuery, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("genres", genrePage.getContent());
        result.put("totalPages", genrePage.getTotalPages());
        result.put("currentPage", page);
        return result;
    }


    public Genre findById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Сущности с ID %d не найдено", id)));
    }

    public void save(EntityDto entityDto) {
        Genre genre = genreMapper.toEntity(entityDto);
        genreRepository.save(genre);
        log.info(String.format("Genre %s added", genre.getName()));
    }

    public void update(EntityDto dto) {
        Long id = dto.getId();
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Сущности с ID %d не найдено", id)));
        genre.setName(dto.getName());
        genreRepository.save(genre);
        log.info(String.format("Genre ID %s changed to %s", genre.getId(), genre.getName()));
    }

    public void delete(Long id) {
        genreRepository.delete(findById(id));
        log.info(String.format("Genre with ID %s removed", id));
    }
}

