package by.innowise.moviereview.service;

import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.mapper.GenreMapper;
import by.innowise.moviereview.mapper.GenreMapperImpl;
import by.innowise.moviereview.repository.GenreRepository;
import by.innowise.moviereview.repository.GenreRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepositoryImpl genreRepository, GenreMapperImpl genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    public List<EntityDto> findAll() {
        return genreMapper.toListDto(genreRepository.findAll());
    }

    public Genre findById(Long id)
    {
        return genreRepository.findById(id);
    }

    public void save(EntityDto entityDto) {
        Genre genre = genreMapper.toEntity(entityDto);
        genreRepository.save(genre);
    }

    public void update(EntityDto dto) {
        genreRepository.update(genreMapper.toEntity(dto));
    }

    public void delete(Long id) {
        genreRepository.delete(findById(id));
    }
}

