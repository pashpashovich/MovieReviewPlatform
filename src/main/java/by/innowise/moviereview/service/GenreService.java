package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.EntityCreateDto;
import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.dto.GenreFilterDto;
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

    public Map<String, Object> getGenresWithFilters(GenreFilterDto filter) {
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), Sort.by(filter.getSort()));
        Page<Genre> genrePage = genreRepository.findAllWithFilters(filter.getSearch(), pageable);

        return Map.of(
                "genres", genrePage.getContent(),
                "totalPages", genrePage.getTotalPages(),
                "currentPage", filter.getPage()
        );
    }

    public Genre findById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Сущности с ID %d не найдено", id)));
    }

    public EntityDto save(EntityCreateDto entityCreateDto) {
        Genre genre = genreMapper.toCreateEntity(entityCreateDto);
        Genre saved = genreRepository.save(genre);
        log.info("Genre {} added", genre.getName());
        return genreMapper.toDto(saved);
    }

    public EntityDto update(Long id, EntityCreateDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Сущности с ID %d не найдено", id)));
        genre.setName(dto.getName());
        Genre genre1 = genreRepository.save(genre);
        log.info("Genre ID {} changed to {}", genre.getId(), genre.getName());
        return genreMapper.toDto(genre1);
    }

    public void delete(Long id) {
        genreRepository.delete(findById(id));
        log.info("Genre with ID {} removed", id);
    }
}

