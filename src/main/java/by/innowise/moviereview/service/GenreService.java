package by.innowise.moviereview.service;

import by.innowise.moviereview.dao.GenreDao;
import by.innowise.moviereview.dto.EntityDto;
import by.innowise.moviereview.entity.Genre;
import by.innowise.moviereview.mapper.GenreMapper;
import by.innowise.moviereview.mapper.GenreMapperImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GenreService {

    private static GenreService instance;
    private final GenreDao genreDao;
    private final GenreMapper genreMapper;

    private GenreService() {
        this.genreDao = GenreDao.getInstance();
        this.genreMapper = new GenreMapperImpl();
    }

    public static GenreService getInstance() {
        if (instance == null)
            instance = new GenreService();
        return instance;
    }

    public List<EntityDto> findAll() {
        return genreMapper.toListDto(genreDao.findAll());
    }

    public Genre findById(Long id) {
        return genreDao.findById(id);
    }

    public void save(EntityDto entityDto) {
        Genre genre = genreMapper.toEntity(entityDto);
        genreDao.save(genre);
    }

    public void update(EntityDto dto) {
        genreDao.update(genreMapper.toEntity(dto));
    }

    public void delete(Long id) {
        genreDao.delete(findById(id));
    }
}

